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

    private int size;

    private int page;

    private Long pdServiceId;

    private List<Long> pdServiceVersionIds;

    private List<Long> almProjectIds;

    private String startDate;

    private String endDate;

    private String [] emailAddress;

    private List<String> almProjectUrls;

}
