package com.arms.jira.jiraissuestatus.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class 지라_이슈_상태_데이터_전송_객체 {

    private  String self;

    private String id;

    private String name;

    private String description;
}
