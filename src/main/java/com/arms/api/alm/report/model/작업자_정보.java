package com.arms.api.alm.report.model;

import lombok.*;

import java.util.Objects;

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
    private String serverType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        작업자_정보 that = (작업자_정보) o;
        return Objects.equals(emailAddress, that.emailAddress) &&
                Objects.equals(serverType, that.serverType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress, serverType);
    }
}
