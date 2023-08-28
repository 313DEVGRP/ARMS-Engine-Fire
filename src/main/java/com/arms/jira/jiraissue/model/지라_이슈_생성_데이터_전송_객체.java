package com.arms.jira.jiraissue.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class 지라_이슈_생성_데이터_전송_객체<T> {

    private 지라_이슈_필드_데이터_전송_객체<T> fields;

}
