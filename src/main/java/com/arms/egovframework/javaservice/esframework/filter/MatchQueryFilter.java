package com.arms.egovframework.javaservice.esframework.filter;


import com.arms.egovframework.javaservice.esframework.Filter;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MatchQueryFilter extends Filter<MatchQueryBuilder> {

	private MatchQueryBuilder matchQueryBuilder;

	public MatchQueryFilter(String name, String value){
		if(name!=null&&value!=null){
			QueryBuilders.matchQuery(name, value);
		}
	}

	public MatchQueryFilter(String name, List<?> value){
		if(name!=null&&value!=null){
			this.matchQueryBuilder = QueryBuilders.matchQuery(name, value);
		}
	}

	public MatchQueryFilter(String name, Long value){
		if(name!=null&&value!=null){
			this.matchQueryBuilder = new MatchQueryFilter(name, List.of(value)).matchQueryBuilder;
		}
	}

	public MatchQueryFilter(String name, boolean value){
		if(name!=null){
			this.matchQueryBuilder = QueryBuilders.matchQuery(name, value);
		}
	}

	public MatchQueryFilter(String name, Long[] value){
		if(name!=null&&value!=null){
			this.matchQueryBuilder = QueryBuilders.matchQuery(name
				, Arrays.stream(value).collect(Collectors.toList()));
		}
	}

	@Override
	public AbstractQueryBuilder<MatchQueryBuilder> abstractQueryBuilder() {
		return  matchQueryBuilder;
	}

}
