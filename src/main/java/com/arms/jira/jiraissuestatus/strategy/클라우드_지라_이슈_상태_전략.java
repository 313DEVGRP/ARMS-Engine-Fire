package com.arms.jira.jiraissuestatus.strategy;


import com.arms.jira.cloud.CloudJiraUtils;
import com.arms.jira.info.model.JiraInfoDTO;
import com.arms.jira.info.service.JiraInfo;

import com.arms.jira.jiraissuestatus.model.지라_이슈_상태_데이터_전송_객체;
import com.arms.jira.jiraissuestatus.model.클라우드_지라_이슈_상태_전체_데이터_전송_객체;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;


@Component
public class 클라우드_지라_이슈_상태_전략 implements 지라_이슈_상태_전략 {

    private final Logger 로그 = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JiraInfo jiraInfo;

    @Override
    public List<지라_이슈_상태_데이터_전송_객체> 이슈_상태_전체_목록_가져오기(Long 연결_아이디) throws Exception{

        로그.info("getStatusList 비즈니스 로직 실행");

        JiraInfoDTO found = jiraInfo.loadConnectInfo(연결_아이디);
        WebClient webClient = CloudJiraUtils.createJiraWebClient(found.getUri(), found.getUserId(), found.getPasswordOrToken());

        int maxResult = 200;
        int startAt = 0;
        boolean checkLast = false;

        List<지라_이슈_상태_데이터_전송_객체> 반환할_지라_이슈_상태_데이터전송객체_목록 = new ArrayList<지라_이슈_상태_데이터_전송_객체>();

        while(!checkLast) {
            String endpoint = "/rest/api/3/statuses/search?maxResults="+ maxResult + "&startAt=" + startAt;
            클라우드_지라_이슈_상태_전체_데이터_전송_객체 지라_이슈_상태_조회_결과 = CloudJiraUtils.get(webClient, endpoint, 클라우드_지라_이슈_상태_전체_데이터_전송_객체.class).block();

            반환할_지라_이슈_상태_데이터전송객체_목록.addAll(지라_이슈_상태_조회_결과.getValues());

            if (지라_이슈_상태_조회_결과.getTotal() == 반환할_지라_이슈_상태_데이터전송객체_목록.size()) {
                checkLast = true;
            }
            else {
                startAt += maxResult;
            }
        }

        return 반환할_지라_이슈_상태_데이터전송객체_목록;
    }




}
