package com.arms.api.engine.services;

import com.arms.api.engine.dtos.검색어_검색결과;
import com.arms.api.engine.dtos.검색어_기본_검색_요청;
import com.arms.api.engine.dtos.검색어_날짜포함_검색_요청;
import com.arms.api.engine.models.플루언트디;
import com.arms.api.engine.repositories.플루언트디_저장소;
import com.arms.elasticsearch.util.query.EsQuery;
import com.arms.elasticsearch.util.query.EsQueryBuilder;
import com.arms.elasticsearch.util.query.bool.RangeQueryFilter;
import com.arms.elasticsearch.util.query.query_string.QueryString;
import com.arms.elasticsearch.util.query.sort.SortBy;
import com.arms.elasticsearch.util.query.일반_검색_요청;
import com.arms.elasticsearch.util.query.정렬_요청;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service("플루언트디_서비스")
@AllArgsConstructor
public class 플루언트디_서비스구현체 implements 플루언트디_서비스{

    private final Logger 로그 = LoggerFactory.getLogger(this.getClass());

    private 플루언트디_저장소 플루언트디_저장소;

    public 검색어_검색결과<SearchHit<플루언트디>> 플루언트디_검색(검색어_기본_검색_요청 검색어_기본_검색_요청){
        EsQuery esQuery = new EsQueryBuilder()
            .sort(new SortBy(
                List.of(
                     정렬_요청.builder().필드("_score").정렬기준("desc").build()
                    ,정렬_요청.builder().필드("@timestamp").정렬기준("desc").build()
                )
            ))
            .queryString(new QueryString(검색어_기본_검색_요청.get검색어()));
        SearchHits<플루언트디> 플루언트디_검색결과 = 플루언트디_저장소.search(일반_검색_요청.of(검색어_기본_검색_요청, esQuery).생성());
        검색어_검색결과<SearchHit<플루언트디>> 검색결과_목록 = new 검색어_검색결과<>();
        검색결과_목록.set검색결과_목록(플루언트디_검색결과.getSearchHits());
        검색결과_목록.set결과_총수(플루언트디_검색결과.getTotalHits());
        return 검색결과_목록;
    }

    @Override
    public 검색어_검색결과<SearchHit<플루언트디>> 플루언트디_날짜포함_검색(검색어_날짜포함_검색_요청 검색어_날짜포함_검색_요청) {
        String start_date = null;
        String end_date = null;
        if(검색어_날짜포함_검색_요청.get시작_날짜() != null && !검색어_날짜포함_검색_요청.get시작_날짜().isBlank()) {
            start_date = 검색어_날짜포함_검색_요청.get시작_날짜();
        }
        if(검색어_날짜포함_검색_요청.get끝_날짜() != null &&!검색어_날짜포함_검색_요청.get끝_날짜().isBlank()) {
            end_date = 검색어_날짜포함_검색_요청.get끝_날짜();
        }

        EsQuery esQuery = new EsQueryBuilder()
                .rangeQueryBuilder(new RangeQueryFilter("@timestamp", start_date, end_date,"fromto"))
                .sort(new SortBy(
                    List.of(
                             정렬_요청.builder().필드("_score").정렬기준("desc").build()
                            ,정렬_요청.builder().필드("@timestamp").정렬기준("desc").build()
                    )
                ))
                .queryString(new QueryString(검색어_날짜포함_검색_요청.get검색어()));
        SearchHits<플루언트디> 플루언트디_검색결과 = 플루언트디_저장소.search(일반_검색_요청.of(검색어_날짜포함_검색_요청, esQuery).생성());
        검색어_검색결과<SearchHit<플루언트디>> 검색결과_목록 = new 검색어_검색결과<>();
        검색결과_목록.set검색결과_목록(플루언트디_검색결과.getSearchHits());
        검색결과_목록.set결과_총수(플루언트디_검색결과.getTotalHits());
        return 검색결과_목록;
    }
}
