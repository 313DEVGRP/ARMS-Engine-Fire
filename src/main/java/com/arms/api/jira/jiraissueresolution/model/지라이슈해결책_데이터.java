package com.arms.api.jira.jiraissueresolution.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class 지라이슈해결책_데이터 {
    private String self;
    private String id;
    private String name;
    private String description;
    private boolean isDefault;
}
