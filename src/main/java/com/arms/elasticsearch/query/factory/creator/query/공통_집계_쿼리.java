package com.arms.elasticsearch.query.factory.creator.query;

import com.arms.elasticsearch.query.EsQuery;
import com.arms.elasticsearch.query.base.기본_집계_요청;
import com.arms.elasticsearch.query.base.하위_집계;
import com.arms.elasticsearch.query.base.하위_집계_요청;
import com.arms.elasticsearch.query.factory.builder.계층_하위_집계_빌더;
import com.arms.elasticsearch.query.factory.builder.비계층_하위_집계_빌더;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.aggregations.support.ValuesSourceAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class 공통_집계_쿼리 <T extends ValuesSourceAggregationBuilder<T>> implements 집계_쿼리 {

    private List<하위_집계> 하위_집계들 = new ArrayList<>();
    private final int 크기;
    private final int 하위크기;
    private final boolean 컨텐츠보기여부;
    private final NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
    private final BoolQueryBuilder boolQuery;
    private final ValuesSourceAggregationBuilder<T> valuesSourceAggregationBuilder;

    protected 공통_집계_쿼리(기본_집계_요청 _일반_집계_요청, EsQuery esQuery, ValuesSourceAggregationBuilder<T> valuesSourceAggregationBuilder){
        this.valuesSourceAggregationBuilder = valuesSourceAggregationBuilder;

        if(_일반_집계_요청 instanceof 하위_집계_요청){
            this.하위_집계들 = Optional.of(((하위_집계_요청) _일반_집계_요청).to_하위_집계_필드들())
                    .filter(a->!a.isEmpty()).orElseGet(()->(((하위_집계_요청) _일반_집계_요청).get_하위_집계_필드들()));
        }
        this.크기 = _일반_집계_요청.get크기();
        this.컨텐츠보기여부 = _일반_집계_요청.is컨텐츠보기여부();
        this.하위크기 = _일반_집계_요청.get하위크기();
        this.boolQuery = esQuery.getQuery(new ParameterizedTypeReference<>() {});
        this.nativeSearchQueryBuilder.withMaxResults(컨텐츠보기여부 ? 크기 : 0);
        this.nativeSearchQueryBuilder.addAggregation(
                this.valuesSourceAggregationBuilder
        );
        List<FieldSortBuilder> fieldSortBuilders = esQuery.getQuery(new ParameterizedTypeReference<>() {
        });

        Optional.ofNullable(boolQuery)
                .ifPresent(query-> this.nativeSearchQueryBuilder.withQuery(boolQuery));

        Optional.ofNullable(fieldSortBuilders)
                .ifPresent(sorts -> sorts.forEach(nativeSearchQueryBuilder::withSort));
    }

    @Override
    public void 계층_하위_집계_빌더_적용(){
        Optional.ofNullable(하위_집계들)
            .ifPresent(__하위_그룹_필드들->{
                if(!__하위_그룹_필드들.isEmpty()){
                    valuesSourceAggregationBuilder
                        .subAggregation(
                            new 계층_하위_집계_빌더().createAggregation(__하위_그룹_필드들,하위크기)
                        );
                }
            });
    }

    @Override
    public void 형제_하위_집계_빌더_적용(){
        Optional.ofNullable(하위_집계들)
            .ifPresent(__하위_그룹_필드들->{
                if(!__하위_그룹_필드들.isEmpty()){
                    new 비계층_하위_집계_빌더().createAggregation(__하위_그룹_필드들,하위크기)
                            .forEach(valuesSourceAggregationBuilder::subAggregation);
                }
            });
    }

    @Override
    public NativeSearchQuery 생성(){
        return nativeSearchQueryBuilder.build();
    }
}
