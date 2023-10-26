package com.arms.elasticsearch.models;

import com.arms.elasticsearch.util.쿼리_추상_팩토리;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class 지라이슈_검색_일자별_요청 implements 쿼리_추상_팩토리 {

	private Long 서비스아이디;
	private String 특정필드;
	private String 특정필드검색어;
	private List<String> 하위그룹필드들;
	private String 시간그룹필드;
	private int 크기 = 1000;
	private boolean 컨텐츠보기여부 = false;
	private boolean 요구사항인지여부;
	private String 필터필드;
	private List<?> 필터필드검색어;

	@Override
	public NativeSearchQuery 생성() {

		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		searchService(boolQuery);
		isReqQuery(boolQuery);
		searchField(boolQuery);
		searchFilter(boolQuery);
		서브_집계_요청 서브_집계_요청 = new 서브_집계_요청(하위그룹필드들, 크기);

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
			.withQuery(boolQuery)
			.withMaxResults(컨텐츠보기여부 ? 크기 : 0);

		Optional.ofNullable(시간그룹필드)
			.ifPresent(시간그룹필드 -> {
				DateHistogramAggregationBuilder dateHistogramAggregationBuilder = new DateHistogramAggregationBuilder(
					"date_group_by_" + 시간그룹필드)
					.field(시간그룹필드)
					.calendarInterval(DateHistogramInterval.DAY)
					.minDocCount(0); // 집계 간격을 지정
				nativeSearchQueryBuilder.withAggregations(
					dateHistogramAggregationBuilder
				);
				Optional.ofNullable(하위그룹필드들)
					.ifPresent(하위그룹필드들->{
						if(!하위그룹필드들.isEmpty()){
							dateHistogramAggregationBuilder.subAggregation(서브_집계_요청.createNestedAggregation(하위그룹필드들, 크기));
						}
					});
			});

		return nativeSearchQueryBuilder.build();

	}


	public BoolQueryBuilder searchService(BoolQueryBuilder boolQuery){

		if(!ObjectUtils.isEmpty(서비스아이디)){
			boolQuery.must(QueryBuilders.termQuery("pdServiceId", 서비스아이디));
		}
		return boolQuery;
	}

	public BoolQueryBuilder searchField(BoolQueryBuilder boolQuery){

		if(!ObjectUtils.isEmpty(특정필드)){
			boolQuery.must(QueryBuilders.termQuery(특정필드, 특정필드검색어));
		}
		return boolQuery;
	}

	public BoolQueryBuilder isReqQuery(BoolQueryBuilder boolQuery){
		if(!ObjectUtils.isEmpty(특정필드)){
			boolQuery.must(QueryBuilders.termQuery("isReq", 특정필드));
		}
		return boolQuery;
	}

	public BoolQueryBuilder searchFilter(BoolQueryBuilder boolQuery){
		if(!ObjectUtils.isEmpty(필터필드)){
			boolQuery.filter(QueryBuilders.termsQuery(필터필드, 필터필드검색어));
		}
		return boolQuery;
	}


}
