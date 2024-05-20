package com.arms.api.util.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class 지라이슈_일자별_제품_및_제품버전_검색__집계_하위_요청 extends 지라이슈_제품_및_제품버전_검색__집계_하위_요청 {
    private String 일자기준;        // created, updated 등 일자 데이터
    private String 시작일;         // 시작일 포맷 "2023-12-17", 시작일 혹은 종료일 하나라도 null일 경우 전체 검색
    private String 종료일;         // 종료일 포맷 "2023-12-17", 시작일 혹은 종료일 하나라도 null일 경우 전체 검색
}
