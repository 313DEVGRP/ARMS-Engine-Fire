package com.arms.elasticsearch.query.factory;

import com.arms.elasticsearch.query.EsQuery;
import com.arms.elasticsearch.query.base.하위_집계_요청;
import com.arms.elasticsearch.query.factory.query.일반_집계_쿼리;
import com.arms.elasticsearch.query.쿼리_생성기;
import lombok.Setter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

@Setter
public class 하위_계층_집계_쿼리_생성기 implements 쿼리_생성기 {
	private final 일반_집계_쿼리 _일반_집계_쿼리;

	private 하위_계층_집계_쿼리_생성기(하위_집계_요청 하위_집계_요청, EsQuery esQuery){
		this._일반_집계_쿼리 = 일반_집계_쿼리.of(하위_집계_요청, esQuery);
	}

	public static 쿼리_생성기 of(하위_집계_요청 하위_집계_요청, EsQuery esQuery){
		return new 하위_계층_집계_쿼리_생성기(하위_집계_요청, esQuery);
	}

	@Override
	public NativeSearchQuery 생성() {
		_일반_집계_쿼리.계층_하위_집계_빌더_적용();
		return _일반_집계_쿼리.생성();
	}

}
