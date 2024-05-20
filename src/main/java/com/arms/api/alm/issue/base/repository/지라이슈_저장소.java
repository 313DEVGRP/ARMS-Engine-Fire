package com.arms.api.alm.issue.base.repository;

import com.arms.api.alm.issue.base.model.지라이슈_엔티티;
import com.arms.elasticsearch.query.EsQuery;
import com.arms.elasticsearch.query.base.기본_검색_요청;
import com.arms.elasticsearch.query.esquery.EsQueryBuilder;
import com.arms.elasticsearch.query.esquery.esboolquery.must.MustTermQuery;
import com.arms.elasticsearch.query.factory.creator.old.일반_검색_쿼리_생성기;
import com.arms.elasticsearch.query.filter.TermsQueryFilter;
import com.arms.elasticsearch.repository.공통저장소;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface 지라이슈_저장소 extends 공통저장소<지라이슈_엔티티,String>{

    default List<지라이슈_엔티티> findByPdServiceIdAndPdServiceVersionsIn(Long pdServiceLink, Long[] pdServiceVersionLinks){
        EsQuery esQuery = new EsQueryBuilder()
            .bool(
                new MustTermQuery("pdServiceId", pdServiceLink),
                new TermsQueryFilter("pdServiceVersions", pdServiceVersionLinks)
            );
        기본_검색_요청 기본_검색_요청 = new 기본_검색_요청() {};
        기본_검색_요청.set크기(10000);
        return this.normalSearch(일반_검색_쿼리_생성기.of(기본_검색_요청,esQuery).생성());
    };

    default List<지라이슈_엔티티> findByIsReqAndPdServiceIdAndPdServiceVersionsIn(boolean isReq, Long pdServiceLink
        , Long[] pdServiceVersionLinks){
        EsQuery esQuery = new EsQueryBuilder()
            .bool(
                new MustTermQuery("isReq", isReq),
                new MustTermQuery("pdServiceId", pdServiceLink),
                new TermsQueryFilter("pdServiceVersions", pdServiceVersionLinks)
            );
        기본_검색_요청 기본_검색_요청 = new 기본_검색_요청() {};
        기본_검색_요청.set크기(10000);
        return this.normalSearch(일반_검색_쿼리_생성기.of(기본_검색_요청,esQuery).생성());
    };

    default List<지라이슈_엔티티> findByParentReqKeyIn(List<String> parentReqKeys){
        EsQuery esQuery = new EsQueryBuilder()
            .bool(
                new TermsQueryFilter("parentReqKey", parentReqKeys)
            );
        기본_검색_요청 기본_검색_요청 = new 기본_검색_요청() {};
        기본_검색_요청.set크기(10000);
        return this.normalSearch(일반_검색_쿼리_생성기.of(기본_검색_요청,esQuery).생성());
    };
}
