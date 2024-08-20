package com.arms.api.alm.report.service;

import com.arms.api.alm.issue.base.model.dto.지라이슈_엔티티;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("dev")
class 리포트_서비스_프로세스Test {

    @Autowired
    private 리포트_서비스 리포트_서비스;

    @Test
    public void 지라이슈_목록_가져오기_test(){
        List<지라이슈_엔티티> 지라이슈_목록_가져오기 = 리포트_서비스.pdServiceId_조건으로_지라이슈_목록_가져오기(22L);
        assert !지라이슈_목록_가져오기.isEmpty();
        System.out.println(지라이슈_목록_가져오기);
    }

}