package com.arms.api.engine.model;

import com.arms.elasticsearch.base.기본_검색_요청;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class 지라이슈_일반_검색_요청 extends 기본_검색_요청 {
	private IsReqType isReqType;
	private List<Long> pdServiceVersionLinks;
}
