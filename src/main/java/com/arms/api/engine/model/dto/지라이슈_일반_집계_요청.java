package com.arms.api.engine.model.dto;

import com.arms.api.engine.model.enums.IsReqType;
import com.arms.elasticsearch.query.base.기본_집계_요청;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class 지라이슈_일반_집계_요청 extends 기본_집계_요청 {

	private IsReqType isReqType;
	private Boolean isReq;
	private List<Long> pdServiceVersionLinks;

}
