package com.arms.api.engine.repository;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ActiveProfiles;

import com.arms.api.alm.issue.base.model.dto.지라이슈_엔티티;

@ActiveProfiles("dev")
@SpringBootTest
public class 이슈인덱스_저장소Test {


	@Autowired
	private com.arms.api.alm.issue.base.repository.지라이슈_저장소 지라이슈_저장소;

	@Test
	public void test(){

		Long cReqLink = 10L;

		SearchHits<지라이슈_엔티티> ids = 지라이슈_저장소.searchHits(
			new NativeSearchQueryBuilder()
				.withQuery(QueryBuilders.termQuery("id", "4491399083726213931_PHM_PHM-125"))
				.build());

		for (SearchHit<지라이슈_엔티티> id : ids) {
			지라이슈_엔티티 content = id.getContent();
			content.setCReqLink(cReqLink);
			지라이슈_저장소.updateSave(content,id.getIndex());
		}

	}
}
