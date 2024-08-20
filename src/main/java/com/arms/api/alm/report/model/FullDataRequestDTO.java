package com.arms.api.alm.report.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullDataRequestDTO {
    private Long pdServiceId;
    private List<Long> pdServiceVersionIds;
    private List<Long> almProjectIds;
    private Long startDate;
    private Long endDate;
    private String accountId;
}
