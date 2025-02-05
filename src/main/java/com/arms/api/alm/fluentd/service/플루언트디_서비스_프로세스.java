package com.arms.api.alm.fluentd.service;

import com.arms.api.util.model.dto.response.검색어_검색결과;
import com.arms.api.util.model.dto.request.검색어_날짜포함_검색_요청;
import com.arms.api.util.model.dto.request.검색어_검색_집계_하위_요청;
import com.arms.api.alm.fluentd.model.플루언트디_엔티티;
import com.arms.egovframework.javaservice.esframework.EsQuery;
import com.arms.egovframework.javaservice.esframework.model.dto.기본_검색_요청;
import com.arms.egovframework.javaservice.esframework.model.dto.기본_정렬_요청;
import com.arms.egovframework.javaservice.esframework.factory.creator.query.쿼리_생성기;
import com.arms.egovframework.javaservice.esframework.filter.QueryStringFilter;
import com.arms.egovframework.javaservice.esframework.filter.RangeQueryFilter;
import com.arms.egovframework.javaservice.esframework.esquery.EsQueryBuilder;
import com.arms.egovframework.javaservice.esframework.factory.creator.기본_쿼리_생성기;
import com.arms.egovframework.javaservice.esframework.factory.creator.중첩_집계_쿼리_생성기;
import com.arms.api.alm.fluentd.repository.*;
import com.arms.egovframework.javaservice.esframework.esquery.EsSortQuery;

import com.arms.egovframework.javaservice.esframework.filter.TermsQueryFilter;
import com.arms.egovframework.javaservice.esframework.model.vo.버킷_집계_결과_목록_합계;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("플루언트디_서비스")
@AllArgsConstructor
public class 플루언트디_서비스_프로세스 implements 플루언트디_서비스{


    private 플루언트디_저장소 플루언트디_저장소;

    @Override
    public 버킷_집계_결과_목록_합계 전체_집계결과_가져오기(쿼리_생성기 쿼리_생성기) {

        return 플루언트디_저장소.전체버킷집계(
                쿼리_생성기.생성()
        );
    }

    @Override
    public 버킷_집계_결과_목록_합계 집계결과_가져오기(쿼리_생성기 쿼리_생성기) {

        return 플루언트디_저장소.버킷집계(
                쿼리_생성기.생성()
        );
    }

    @Override
    public 검색어_검색결과<SearchHit<플루언트디_엔티티>> 플루언트디_날짜포함_검색(검색어_날짜포함_검색_요청 검색어_날짜포함_검색_요청) {

        String start_date = null;
        String end_date = null;

        if(검색어_날짜포함_검색_요청.get시작_날짜() != null && !검색어_날짜포함_검색_요청.get시작_날짜().isBlank()) {
            start_date = 검색어_날짜포함_검색_요청.get시작_날짜();
        }
        if(검색어_날짜포함_검색_요청.get끝_날짜() != null &&!검색어_날짜포함_검색_요청.get끝_날짜().isBlank()) {
            end_date = 검색어_날짜포함_검색_요청.get끝_날짜();
        }

        EsQuery esQuery = new EsQueryBuilder()
            .bool(
                 new RangeQueryFilter("@timestamp", start_date, end_date,"fromto")
                ,new QueryStringFilter(검색어_날짜포함_검색_요청.get검색어())
            )
            .sort(new EsSortQuery(
                List.of(
                    기본_정렬_요청.builder().필드("@timestamp").정렬_기준("desc").build()
                )
            ));

        SearchHits<플루언트디_엔티티> 플루언트디_검색결과
            = 플루언트디_저장소.searchHits(기본_쿼리_생성기.기본검색(검색어_날짜포함_검색_요청, esQuery).생성());

        검색어_검색결과<SearchHit<플루언트디_엔티티>> 검색결과_목록 = new 검색어_검색결과<>();
        if(플루언트디_검색결과 != null && !플루언트디_검색결과.isEmpty() ) {
            검색결과_목록.set검색결과_목록(플루언트디_검색결과.getSearchHits());
            검색결과_목록.set결과_총수(플루언트디_검색결과.getTotalHits());
        }
        return 검색결과_목록;
    }


    @Override
    public 버킷_집계_결과_목록_합계 플루언트디_로그네임_집계(검색어_검색_집계_하위_요청 검색어_집계_요청) {
        String start_date = null;
        String end_date = null;
        if(검색어_집계_요청.get시작_날짜() != null && !검색어_집계_요청.get시작_날짜().isBlank()) {
            start_date = 검색어_집계_요청.get시작_날짜();
        }
        if(검색어_집계_요청.get끝_날짜() != null &&!검색어_집계_요청.get끝_날짜().isBlank()) {
            end_date = 검색어_집계_요청.get끝_날짜();
        }

        EsQuery esQuery = new EsQueryBuilder()
            .bool(
                 new RangeQueryFilter("@timestamp", start_date, end_date,"fromto")
                ,new QueryStringFilter(검색어_집계_요청.get검색어())
            );

        버킷_집계_결과_목록_합계 집계_결과 = this.전체_집계결과_가져오기(중첩_집계_쿼리_생성기.포괄(검색어_집계_요청, esQuery));
        return 집계_결과;
    }


    @Override
    public void 커넥션_상태_유지(){
        log.info("엘라스틱서치 커넥션 상태 유지");
        EsQuery esQuery = new EsQueryBuilder()
            .bool(
                new TermsQueryFilter("id", "313")
            );
        기본_검색_요청 기본_검색_요청 = new 기본_검색_요청() {
        };
        기본_검색_요청.set페이지_처리_여부(false);
        기본_검색_요청.set크기(1);
        플루언트디_저장소.normalSearchList(기본_쿼리_생성기.기본검색(기본_검색_요청,esQuery).생성());
    }
}
