package com.arms.api.alm.report.model;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class 작업자_정보 {

    private String accountId;
    private String emailAddress;
    private String displayName;
}
