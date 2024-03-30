package com.arms.api.engine.model.vo;

import static java.util.stream.Collectors.*;

import java.util.List;

import com.arms.elasticsearch.버킷_집계_결과;

import lombok.Getter;

@Getter
public class 제품_서비스_버전 {
	private final Long 제품_서비스_버전;
	private final List<요구_사항> 요구사항들;

	public 제품_서비스_버전(버킷_집계_결과 버킷_집계_결과, 하위_이슈_사항들 하위_이슈_사항들) {
		this.제품_서비스_버전 = Long.parseLong(버킷_집계_결과.get필드명());
		this.요구사항들 = 버킷_집계_결과.get하위검색결과()
			.entrySet()
			.stream()
			.flatMap(a -> a.getValue().stream())
			.map(a -> new 요구_사항(a, 하위_이슈_사항들))
			.collect(toList());
	}
}
