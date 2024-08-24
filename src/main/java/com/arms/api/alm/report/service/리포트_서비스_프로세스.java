package com.arms.api.alm.report.service;

import com.arms.api.alm.issue.base.model.dto.지라이슈_엔티티;
import com.arms.api.alm.issue.base.repository.지라이슈_저장소;
import com.arms.api.alm.report.model.FullDataRequestDTO;
import com.arms.api.alm.report.model.FullDataResponseDTO;
import com.arms.api.alm.report.model.작업자_정보;
import com.arms.egovframework.javaservice.esframework.EsQuery;
import com.arms.egovframework.javaservice.esframework.esquery.EsQueryBuilder;
import com.arms.egovframework.javaservice.esframework.factory.creator.기본_쿼리_생성기;
import com.arms.egovframework.javaservice.esframework.filter.ExistsQueryFilter;
import com.arms.egovframework.javaservice.esframework.filter.RangeQueryFilter;
import com.arms.egovframework.javaservice.esframework.filter.TermsQueryFilter;
import com.arms.egovframework.javaservice.esframework.model.dto.기본_검색_요청;
import com.arms.egovframework.javaservice.esframework.must.TermQueryMust;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("리포트_서비스")
@AllArgsConstructor
public class 리포트_서비스_프로세스 implements 리포트_서비스{

    private 지라이슈_저장소 지라이슈_저장소;

    @Override
//    public List<지라이슈_엔티티> 작업자_정보_목록_가져오기(
    public List<작업자_정보> 작업자_정보_목록_가져오기(FullDataRequestDTO fullDataRequestDTO) {

        EsQuery esQuery = new EsQueryBuilder()
                .bool(
                    new TermQueryMust("pdServiceId", fullDataRequestDTO.getPdServiceId()),
                    new TermsQueryFilter("pdServiceVersions", fullDataRequestDTO.getPdServiceVersionIds()),
                    new ExistsQueryFilter("assignee")
                    // alm_project 에 관한 부분까지
                    // 설정에 따라서, 제품-버전까지, 제품-버전-프로젝트, 제품-버전-프로젝트-기간 까지
                );

        List<지라이슈_엔티티> 작업자_목록_검색_결과 =
                지라이슈_저장소.normalSearchList(기본_쿼리_생성기.기본검색(new 기본_검색_요청() {}, esQuery).생성());

        // 담당자 정보 중복 제거를 위한 해시세트
        Map<String, 작업자_정보> uniqueAssignees = new HashMap<>();

        for (지라이슈_엔티티 검색결과 : 작업자_목록_검색_결과 ) {
            지라이슈_엔티티.담당자 assignee = 검색결과.getAssignee();
            if (assignee != null) {
                String email = assignee.getEmailAddress();
                String accountId = assignee.getAccountId();
                String name = assignee.getDisplayName();
                // email 이 null 일 경우, key로 accountId로 대체
                // redmine 에서 accountId가 "6"과 같이 중복 가능성이 있어보이므로 accountId+"-"+name 으로 처리.
                String key = (email != null) ? email : accountId+"-"+name;
                if (!uniqueAssignees.containsKey(key)) {
                    작업자_정보 작업자 = new 작업자_정보(
                            assignee.getAccountId(),
                            email,
                            assignee.getDisplayName()
                    );
                    uniqueAssignees.put(email, 작업자);
                }
            }
        }

        List<작업자_정보> 작업자_리스트 = new ArrayList<>(uniqueAssignees.values());


        return 작업자_리스트;
    }

    @Override
    public FullDataResponseDTO pdServiceId_조건으로_이슈_목록_가져오기(FullDataRequestDTO fullDataRequestDTO) {

        EsQuery esQuery = new EsQueryBuilder()
            .bool(
                new TermsQueryFilter("pdServiceId", fullDataRequestDTO.getPdServiceId()),
                new TermsQueryFilter("pdServiceVersion", fullDataRequestDTO.getPdServiceVersionIds()),
                new TermsQueryFilter("project.project_self", fullDataRequestDTO.getAlmProjectIds()),
                new TermsQueryFilter("assignee.assignee_emailAddress", fullDataRequestDTO.getEmailAddress()),
                RangeQueryFilter.of("create_date")
                    .from(fullDataRequestDTO.getStartDate())
                    .to(fullDataRequestDTO.getEndDate())
            );

        기본_검색_요청 기본_검색_요청 = new 기본_검색_요청();
        기본_검색_요청.set페이지(fullDataRequestDTO.getPage());
        기본_검색_요청.set크기(fullDataRequestDTO.getSize());
        SearchHits<지라이슈_엔티티> searchHits = 지라이슈_저장소.normalSearchHits(기본_쿼리_생성기.기본검색(기본_검색_요청, esQuery).생성());

        FullDataResponseDTO 검색결과 = new FullDataResponseDTO();
        if (searchHits != null && searchHits.getTotalHits() != 0L) {
            List<지라이슈_엔티티> 이슈_엔티티_목록
                    = searchHits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
            검색결과.setIssueEntityList(이슈_엔티티_목록);
            검색결과.setTotalHits(searchHits.getTotalHits());
            return 검색결과;
        } else {
            검색결과.setTotalHits(0L);
            검색결과.setIssueEntityList(Collections.emptyList());
            return 검색결과;
        }
    }

}
