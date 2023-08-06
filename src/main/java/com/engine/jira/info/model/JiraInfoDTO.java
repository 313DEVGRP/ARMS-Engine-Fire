package com.engine.jira.info.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JiraInfoDTO {
    private String connectId;
    private String userId;
    private String passwordOrToken;
    private String uri;
}
