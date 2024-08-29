package com.arms.api.alm.report.service;

import com.arms.api.alm.issue.base.model.dto.지라이슈_엔티티;
import com.arms.api.alm.issue.base.repository.지라이슈_저장소;
import com.arms.api.alm.report.model.FullDataRequestDTO;
import com.arms.api.alm.report.model.FullDataResponseDTO;
import com.arms.api.alm.report.model.작업자_정보;
import com.arms.api.alm.serverinfo.service.서버정보_서비스;
import com.arms.egovframework.javaservice.esframework.EsQuery;
import com.arms.egovframework.javaservice.esframework.esquery.EsQueryBuilder;
import com.arms.egovframework.javaservice.esframework.factory.creator.기본_쿼리_생성기;
import com.arms.egovframework.javaservice.esframework.filter.ExistsQueryFilter;
import com.arms.egovframework.javaservice.esframework.filter.RangeQueryFilter;
import com.arms.egovframework.javaservice.esframework.filter.TermsQueryFilter;
import com.arms.egovframework.javaservice.esframework.model.dto.기본_검색_요청;
import com.arms.egovframework.javaservice.esframework.model.dto.기본_검색_집계_하위_요청;
import com.arms.egovframework.javaservice.esframework.model.vo.버킷_집계_결과;
import com.arms.egovframework.javaservice.esframework.model.vo.버킷_집계_결과_목록_합계;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.arms.egovframework.javaservice.esframework.factory.creator.중첩_집계_쿼리_생성기.포괄;

@Slf4j
@Service("리포트_서비스")
@AllArgsConstructor
public class 리포트_서비스_프로세스 implements 리포트_서비스{

    private 지라이슈_저장소 지라이슈_저장소;

    private 서버정보_서비스 서버정보_서비스;

    @Override
    public List<작업자_정보> 작업자_목록_가져오기() {

        Map<String, String> 서버_연결아이디_유형_맵 = 서버정보_서비스.서버_연결아이디_유형_맵();

        Arrays.asList("jira_server_id","assignee.assignee_displayName.keyword");

        기본_검색_집계_하위_요청 집계_요청 = new 기본_검색_집계_하위_요청();
        집계_요청.set메인_그룹_필드("assignee.assignee_emailAddress.keyword");
        집계_요청.set하위_그룹_필드들(List.of("jira_server_id","assignee.assignee_displayName.keyword","assignee.assignee_accountId.keyword"));

        EsQuery esQuery = new EsQueryBuilder()
                .bool( new ExistsQueryFilter("assignee") );

        버킷_집계_결과_목록_합계 버킷집계 = 지라이슈_저장소.버킷집계(포괄(집계_요청, esQuery).생성());
        Set<작업자_정보> 작업자_목록 = new HashSet<>();
        if (버킷집계.get전체합계() == 0) {
            log.info("[ 리포트_서비스_프로세스 :: 작업자_목록_가져오기 ] :: 작업자 정보가 없습니다. 사이즈 => 0");
        } else {

            List<버킷_집계_결과> 이메일집계_목록 = 버킷집계.get검색결과().get("group_by_assignee.assignee_emailAddress.keyword");
            log.info("[ 리포트_서비스_프로세스 :: 작업자_목록_가져오기 ] :: 이메일 계정 수 => {}}", 이메일집계_목록.size());
            for (버킷_집계_결과 이메일 : 이메일집계_목록) {

                String 이메일_정보 = 이메일.get필드명();

                if( 이메일.get하위검색결과().containsKey("group_by_jira_server_id") ) {
                    List<버킷_집계_결과> 이메일_연동_서버연결아이디_목록 = 이메일.get하위검색결과().get("group_by_jira_server_id");
                    if(이메일_연동_서버연결아이디_목록.isEmpty()) {
                        log.info("[ 리포트_서비스_프로세스 :: 작업자_목록_가져오기 ] :: 해당 이메일에 연결된 서버연결아이디 없음");
                    } else {
                        for(버킷_집계_결과 연결아이디_이름_계정정보_객체 : 이메일_연동_서버연결아이디_목록) {

                            작업자_정보 작업자 = new 작업자_정보();

                            String 연결아이디 = 연결아이디_이름_계정정보_객체.get필드명();
                            String 서버_유형 = 서버_연결아이디_유형_맵.get(연결아이디);

                            String 작업자_성명 = "";
                            String 작업자_계정 = "";

                            작업자.setEmailAddress(이메일_정보);
                            작업자.setServerType(서버_유형);
                            // 작업자_성명
                            if(연결아이디_이름_계정정보_객체.get하위검색결과().containsKey("group_by_assignee.assignee_displayName.keyword")) {
                                List<버킷_집계_결과> 작업자_성명_목록 = 연결아이디_이름_계정정보_객체.get하위검색결과().get("group_by_assignee.assignee_displayName.keyword");
                                작업자_성명 = 작업자_성명_목록.get(0).get필드명();
                            }
                            // 작업자_계정
                            if(연결아이디_이름_계정정보_객체.get하위검색결과().containsKey("group_by_assignee.assignee_accountId.keyword")) {
                                List<버킷_집계_결과> 작업자_계정_목록 = 연결아이디_이름_계정정보_객체.get하위검색결과().get("group_by_assignee.assignee_accountId.keyword");
                                작업자_계정 = 작업자_계정_목록.get(0).get필드명();
                            }

                            작업자.setDisplayName(작업자_성명);
                            작업자.setAccountId(작업자_계정);

                            작업자_목록.add(작업자);
                        }
                    }
                }

            }
        }

        if(작업자_목록.isEmpty()) {
            return new ArrayList<>(Collections.emptyList());
        } else {
            return 작업자_목록.stream().collect(Collectors.toList());
        }
    }

    @Override
    public FullDataResponseDTO pdServiceId_조건으로_이슈_목록_가져오기(FullDataRequestDTO fullDataRequestDTO) {

        EsQuery esQuery = new EsQueryBuilder()
            .bool(
                new TermsQueryFilter("pdServiceId", fullDataRequestDTO.getPdServiceId()),
                new TermsQueryFilter("pdServiceVersion", fullDataRequestDTO.getPdServiceVersionIds()),
                new TermsQueryFilter("project.project_self.keyword", fullDataRequestDTO.getAlmProjectUrls()),
                new TermsQueryFilter("assignee.assignee_emailAddress.keyword", fullDataRequestDTO.getEmailAddress()),
                RangeQueryFilter.of("created")
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
