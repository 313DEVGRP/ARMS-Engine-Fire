package com.arms.api.alm.report.service;

import com.arms.api.alm.report.model.FullDataRequestDTO;
import com.arms.api.alm.report.model.FullDataResponseDTO;
import com.arms.api.alm.report.model.작업자_정보;

import java.util.List;

public interface 리포트_서비스 {

    // 현재는 _source 를 지정할 수 없으므로 자체 변환하여 사용
    List<작업자_정보> 작업자_정보_목록_가져오기(FullDataRequestDTO fullDataRequestDTO);
    //List<지라이슈_엔티티> 작업자_정보_목록_가져오기(Long pdServiceId, Long[] pdServiceVersions, String project_name);

    FullDataResponseDTO pdServiceId_조건으로_이슈_목록_가져오기(FullDataRequestDTO fullDataRequestDTO);
}
