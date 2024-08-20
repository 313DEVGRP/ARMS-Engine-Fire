package com.arms.api.alm.report.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@ToString
public class FullDataDTO {

	private Long pdServiceLink;

	private Long [] pdServiceVersionLinks;

	private String [] almProjectLinks;

	private String startDate;

	private String endDate;

	private String [] emailAddress;

}
