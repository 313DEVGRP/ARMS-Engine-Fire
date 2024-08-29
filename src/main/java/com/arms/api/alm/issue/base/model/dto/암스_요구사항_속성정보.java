package com.arms.api.alm.issue.base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class 암스_요구사항_속성정보 {

    private Long cReqLink;
    private Long pdServiceId;
    private Long[] pdServiceVersions;
    private Long cReqPriorityLink;
    private String cReqPriorityName;
    private Long cReqDifficultyLink;
    private String cReqDifficultyName;
    private Long cReqStateLink;
    private String cReqStateName;
}
