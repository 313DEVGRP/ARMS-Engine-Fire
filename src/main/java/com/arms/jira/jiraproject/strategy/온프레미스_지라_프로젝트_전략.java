package com.arms.jira.jiraproject.strategy;

import com.arms.jira.info.model.JiraInfoDTO;
import com.arms.jira.info.service.지라연결_서비스;
import com.arms.jira.jiraproject.model.지라_프로젝트_데이터_전송_객체;
import com.arms.jira.utils.지라유틸;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class 온프레미스_지라_프로젝트_전략 implements 지라_프로젝트_전략 {

    private final Logger 로그 = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private 지라연결_서비스 지라연결_서비스;

    @Override
    public 지라_프로젝트_데이터_전송_객체 프로젝트_상세정보_가져오기(Long 연결_아이디, String 프로젝트_키_또는_아이디) throws URISyntaxException, IOException {
        로그.info("온프레미스 지라 프로젝트 "+ 프로젝트_키_또는_아이디 +" 상세정보 가져오기");

        JiraInfoDTO 연결정보 = 지라연결_서비스.checkInfo(연결_아이디);
        JiraRestClient restClient = 지라유틸.온프레미스_통신기_생성(연결정보.getUri(),
                                                                            연결정보.getUserId(),
                                                                            연결정보.getPasswordOrToken());

        BasicProject 온프레미스_지라_프로젝트 = restClient.getProjectClient().getProject(프로젝트_키_또는_아이디).claim();

        지라_프로젝트_데이터_전송_객체 반환할_지라_프로젝트_상세정보 = new 지라_프로젝트_데이터_전송_객체();
        반환할_지라_프로젝트_상세정보.setSelf(온프레미스_지라_프로젝트.getSelf().toString());
        반환할_지라_프로젝트_상세정보.setId(온프레미스_지라_프로젝트.getId().toString());
        반환할_지라_프로젝트_상세정보.setKey(온프레미스_지라_프로젝트.getKey());
        반환할_지라_프로젝트_상세정보.setName(온프레미스_지라_프로젝트.getName());

        return 반환할_지라_프로젝트_상세정보;

    }

    @Override
    public List<지라_프로젝트_데이터_전송_객체> 프로젝트_전체_목록_가져오기(Long 연결_아이디) throws URISyntaxException, IOException {

        로그.info("온프레미스 지라 프로젝트 전체목록 가져오기");

        JiraInfoDTO 연결정보 = 지라연결_서비스.checkInfo(연결_아이디);
        JiraRestClient restClient = 지라유틸.온프레미스_통신기_생성(연결정보.getUri(),
                                                                        연결정보.getUserId(),
                                                                        연결정보.getPasswordOrToken());

        Iterable<BasicProject> 모든_온프레미스_프로젝트 = restClient.getProjectClient().getAllProjects().claim();
        List<지라_프로젝트_데이터_전송_객체> 반환할_지라_프로젝트_목록 = new ArrayList<>();

        for (BasicProject project : 모든_온프레미스_프로젝트) {

            지라_프로젝트_데이터_전송_객체 온프레미스_지라_프로젝트 = new 지라_프로젝트_데이터_전송_객체();
            온프레미스_지라_프로젝트.setSelf(project.getSelf().toString());
            온프레미스_지라_프로젝트.setId(project.getId().toString());
            온프레미스_지라_프로젝트.setKey(project.getKey());
            온프레미스_지라_프로젝트.setName(project.getName());

            반환할_지라_프로젝트_목록.add(온프레미스_지라_프로젝트);
        }

        return 반환할_지라_프로젝트_목록;
    }
}
