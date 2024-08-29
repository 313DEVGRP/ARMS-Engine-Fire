package com.arms.api.alm.report.service;

import com.arms.api.alm.report.model.FullDataRequestDTO;
import com.arms.api.alm.report.model.FullDataResponseDTO;
import com.arms.api.alm.report.model.작업자_정보;
import com.arms.egovframework.javaservice.esframework.model.vo.버킷_집계_결과_목록_합계;

import java.util.List;

public interface 리포트_서비스 {

    List<작업자_정보> 작업자_목록_가져오기();

    FullDataResponseDTO pdServiceId_조건으로_이슈_목록_가져오기(FullDataRequestDTO fullDataRequestDTO);

}
