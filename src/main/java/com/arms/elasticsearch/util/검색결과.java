package com.arms.elasticsearch.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation.Bucket;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode(exclude = "count")
@Getter
@NoArgsConstructor
public class 검색결과 {

    private String 필드명;
    private long 개수;
    private Map<String, List<검색결과>> 하위검색결과;

    public List<검색결과> 하위검색결과(String name){
        return 하위검색결과.get(name);
    }

    public 검색결과(Bucket bucket) {
        this.필드명 = bucket.getKeyAsString();
        this.개수 = bucket.getDocCount();
        this.하위검색결과 = new 검색결과_목록_메인(bucket.getAggregations().asMap()).get검색결과();
    }

    public 검색결과(ParsedFilter parsedFilter) {
        this.개수 = parsedFilter.getDocCount();
        this.하위검색결과 = new 검색결과_목록_메인(parsedFilter.getAggregations().asMap()).get검색결과();
    }

    public Long 필터필드개수(String name){
        return 하위검색결과.get(name).stream().findFirst().map(a->a.get개수()).orElse(0L);
    }

    public String 필터필드명(String name){
        return 하위검색결과.get(name).stream().findFirst().map(a->a.get필드명()).orElse("N/A");
    }

    public Map<String,Long> 검색결과_맵처리(String name){
        return 하위검색결과.get(name).stream()
               .collect(Collectors.toMap(검색결과->검색결과.get필드명(),검색결과->검색결과.get개수(),(p1,p2)->p1));
    }


}
