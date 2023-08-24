package com.arms.jira.jiraissue.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class 지라_이슈_데이터_전송_객체<T> {

    // 공통 필드
    private String id;

    private String key;

    private String self;

    // 특정 이슈 조회 시 사용
    private 지라_이슈_필드_데이터_전송_객체<T> fields;

    // 특정 프로젝트의 전체 이슈 조회 시 사용
    private List<지라_이슈_데이터_전송_객체> issues;

}
