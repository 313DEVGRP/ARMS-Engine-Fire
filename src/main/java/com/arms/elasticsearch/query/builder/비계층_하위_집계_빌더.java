package com.arms.elasticsearch.query.builder;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;

import java.util.List;
import java.util.stream.Collectors;

public class 비계층_하위_집계_빌더 implements 하위_집계_빌더<List<AggregationBuilder>>{


    /**
     * 인자로 받은 하위_그룹필드들 값을 모두 메인 집계 하위에 추가하고 싶은 케이스 대응
     * For example,
     * 담당자 별 (1 depth)
     *  - 요구사항 개수 (2 depth)
     *  - 이슈 개수 (2 depth)
     *  - 이슈 상태 (2 depth)
     *  - 이슈 우선순위 (2 depth)
     *
     * @param 하위_그룹필드들 The list of sub-group fields to be aggregated.
     * @param size The maximum number of unique sub-group field values to be included in the aggregation.
     * @return An {@link AggregationBuilder} that can be used to execute the aggregation.
     */
    @Override
    public List<AggregationBuilder> createAggregation(List<String> 하위그룹필드, int size) {
        return 하위그룹필드.stream()
            .map(하위필드명->{
                return AggregationBuilders.terms("group_by_" + 하위필드명)
                    .field(하위필드명)
                    .size(size);
            }).collect(Collectors.toList());
    }
}
