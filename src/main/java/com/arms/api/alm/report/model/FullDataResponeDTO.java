package com.arms.api.alm.report.model;

import com.arms.api.alm.issue.base.model.dto.지라이슈_엔티티;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullDataResponeDTO {

    Long totalHits;
    List<지라이슈_엔티티> issueEntityList;
}
