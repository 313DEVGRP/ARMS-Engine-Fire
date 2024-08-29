package com.arms.egovframework.javaservice.esframework.repository;

import com.arms.egovframework.javaservice.esframework.EsQuery;
import com.arms.egovframework.javaservice.esframework.annotation.Recent;
import com.arms.egovframework.javaservice.esframework.esquery.EsQueryBuilder;
import com.arms.egovframework.javaservice.esframework.filter.TermsQueryFilter;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.arms.config.ApplicationContextProvider.getBean;
import static com.arms.egovframework.javaservice.esframework.util.ReflectionUtil.fieldInfo;
import static com.arms.egovframework.javaservice.esframework.util.ReflectionUtil.fieldValues;
import static java.util.stream.Collectors.*;

@Slf4j
public class RecentFieldConvertor<T> {

    private final Map<Object, List<SearchHit<T>>> searchHitsMapList;

    private final ElasticsearchOperations operations;

    private final Iterable<? extends T> entities;

    public RecentFieldConvertor(Iterable<T> entities) {


        String recentFieldName = fieldInfo(entities, Recent.class).getName();

        String idFieldName = fieldInfo(entities,Id.class).getName();

        this.entities = entities;

        this.operations = getBean(ElasticsearchOperations.class);

        EsQuery esQuery
                = new EsQueryBuilder()
                .bool(
                        new TermsQueryFilter(idFieldName, fieldValues(entities,Id.class)),
                        new TermsQueryFilter(recentFieldName,true)
                );

        BoolQueryBuilder boolQuery = esQuery.getQuery(new ParameterizedTypeReference<>() {
        });

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();

        Class<T> aClass = StreamSupport.stream(entities.spliterator(), false)
                .map(a -> (Class<T>)a.getClass()).findFirst()
                .orElseThrow(() -> new RuntimeException("잘못된 클래스 접근."));

        this.searchHitsMapList = Optional.ofNullable(this.search(searchQuery, aClass))
                .map(schHits ->
                        schHits.getSearchHits().stream()
                                .filter(a -> a.getIndex() != null)
                                .sorted(Comparator.comparing(SearchHit::getIndex, Comparator.reverseOrder()))
                                .collect(groupingBy(a -> {
                                    try {
                                        return fieldInfo(a.getContent().getClass(), Id.class).get(a.getContent());
                                    } catch (IllegalAccessException e) {
                                        throw new IllegalArgumentException(e);
                                    }
                                }))
                ).orElse(null);
    }

    private SearchHits<T> search(Query query,Class<T> clazz) {
        try{
            return operations.search(query, clazz);
        }catch (NoSuchIndexException e){
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("no such index")) {
                return null;
            }
            throw e;
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    public T recentTrue(T newEntity){
        try {
            Object keyObject = fieldInfo(newEntity.getClass(), Id.class).get(newEntity);

            if(searchHitsMapList!=null && searchHitsMapList.containsKey(keyObject)){

                SearchHit<T> searchHit = searchHitsMapList.get(keyObject)
                        .stream()
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("값이 비워 있습니다."));

                if(newEntity.equals(searchHit.getContent())){
                    return null;
                }else{
                    fieldInfo(newEntity.getClass(), Recent.class).setBoolean(newEntity, true);
                    return newEntity;
                }

            }else{
                fieldInfo(newEntity.getClass(), Recent.class).setBoolean(newEntity, true);
                return newEntity;
            }

        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<SearchHit<T>> recentFalse(Function<Map<Object, List<SearchHit<T>>>,Stream<SearchHit<T>>> function){

        return Optional.ofNullable(searchHitsMapList)
                .map(entitiMap -> function.apply(entitiMap)
                        .map(hit -> {
                            try {
                                fieldInfo(hit.getContent().getClass(), Recent.class)
                                        .setBoolean(hit.getContent(), false);
                                return hit;
                            } catch (IllegalAccessException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }).collect(toList())
                )
                .orElse(null);
    }

    public List<SearchHit<T>> recentFalseIfNotEqual(T newEntity){

        Function<Map<Object, List<SearchHit<T>>>,Stream<SearchHit<T>>> function = (entitiMap)-> {
            try {
                return entitiMap.getOrDefault(fieldInfo(newEntity.getClass(), Id.class).get(newEntity), Collections.emptyList())
                        .stream()
                        .filter(hit -> !newEntity.equals(hit.getContent()));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        return this.recentFalse(function);
    }

    public List<SearchHit<T>> recentFalseForDuplicateContent(T newEntity){

        Function<Map<Object, List<SearchHit<T>>>,Stream<SearchHit<T>>> function = (entitiMap)-> {
            try {
                return entitiMap.getOrDefault(fieldInfo(newEntity.getClass(), Id.class).get(newEntity), Collections.emptyList())
                        .stream()
                        .filter(hit -> newEntity.equals(hit.getContent()))
                        .skip(1);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        return this.recentFalse(function);
    }

    public Map<String, List<T>> listMapGroupByIndex(Function<T, List<SearchHit<T>>> function){
        return StreamSupport.stream(entities.spliterator(), false)
                .filter(Objects::nonNull)
                .map(function)
                .filter(Objects::nonNull)
                .flatMap(searchHits -> searchHits.stream().filter(a->a.getIndex()!=null))
                .collect(groupingBy(SearchHit::getIndex, mapping(SearchHit::getContent, toList())));
    }
}
