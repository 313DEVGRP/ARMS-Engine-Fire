package com.arms.api.alm.report.service;

import com.arms.api.alm.issue.base.model.dto.지라이슈_엔티티;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;


@SpringBootTest
@ActiveProfiles("dev")
class 리포트_서비스_프로세스Test {

    @Autowired
    private 리포트_서비스 리포트_서비스;

    /*1. pdService와 pdServiceVersion 정보 가져오기
    T_ARMS_PDSERVICE
2. 요구사항 명과 요구사항 상태 c_id 가져오기 ( []는 pdService의 c_id)
    T_ARMS_REQADD_[]
3. 요구사항 상태 테이블
            T_ARMS_REQSTATE
    입니다*/

    @Test
    public void 지라이슈_목록_가져오기_test(){
        List<지라이슈_엔티티> 지라이슈_목록_가져오기 = 리포트_서비스.pdServiceId_조건으로_이슈_목록_가져오기(25L);
        assert !지라이슈_목록_가져오기.isEmpty();
        System.out.println(지라이슈_목록_가져오기);
    }

}