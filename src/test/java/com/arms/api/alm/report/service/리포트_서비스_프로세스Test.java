package com.arms.api.alm.report.service;

import com.arms.api.alm.issue.base.model.dto.지라이슈_엔티티;
import com.arms.api.alm.issue.base.repository.지라이슈_저장소;
import com.arms.api.alm.report.model.FullDataDTO;
import com.arms.egovframework.javaservice.esframework.EsQuery;
import com.arms.egovframework.javaservice.esframework.esquery.EsQueryBuilder;
import com.arms.egovframework.javaservice.esframework.factory.creator.기본_쿼리_생성기;
import com.arms.egovframework.javaservice.esframework.filter.RangeQueryFilter;
import com.arms.egovframework.javaservice.esframework.filter.TermsQueryFilter;
import com.arms.egovframework.javaservice.esframework.model.dto.기본_검색_요청;

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
        FullDataDTO fullDataDTO = new FullDataDTO();

        RangeQueryFilter create_date = RangeQueryFilter.of("create_date")
            .from(fullDataDTO.getStartDate())
            .to(fullDataDTO.getEndDate());


        EsQuery esQuery = new EsQueryBuilder()
            .bool(
                new TermsQueryFilter("pdServiceId", fullDataDTO.getPdServiceLink()),
                new TermsQueryFilter("pdServiceVersion", fullDataDTO.getPdServiceVersionLinks()),
                new TermsQueryFilter("project.project_self", fullDataDTO.getAlmProjectLinks()),
                new TermsQueryFilter("assignee.assignee_emailAddress", fullDataDTO.getEmailAddress())
            );

        List<지라이슈_엔티티> 지라이슈_엔티티s = 지라이슈_저장소.normalSearch(기본_쿼리_생성기.기본검색(new 기본_검색_요청() {
        }, esQuery).생성());

        System.out.println(지라이슈_엔티티s);
    }


    @Autowired
    private com.arms.api.alm.issue.base.repository.지라이슈_저장소 지라이슈_저장소;


}
