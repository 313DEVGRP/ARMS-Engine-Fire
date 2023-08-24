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
public class 클라우드_지라_이슈_조회_데이터_전송_객체 {
    private Integer startAt;
    private Integer maxResults;
    private Integer total;
    private List<지라_이슈_데이터_전송_객체> issues;
}
