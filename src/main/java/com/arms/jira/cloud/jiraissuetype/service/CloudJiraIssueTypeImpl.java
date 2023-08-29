package com.arms.jira.cloud.jiraissuetype.service;

import com.arms.jira.utils.지라유틸;
import com.arms.jira.cloud.jiraissuetype.model.CloudJiraIssueTypeDTO;
import com.arms.jira.cloud.jiraissuetype.model.CloudJiraIssueTypeInputDTO;
import com.arms.jira.info.model.JiraInfoDTO;
import com.arms.jira.info.model.JiraInfoEntity;
import com.arms.jira.info.service.지라연결_서비스;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service("cloudJiraIssueType")
@AllArgsConstructor
public class CloudJiraIssueTypeImpl implements CloudJiraIssueType {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private 지라연결_서비스 지라연결_서비스;

    @Override
    public List<CloudJiraIssueTypeDTO> getIssueTypeListAll(Long connectId) throws Exception {

        String endpoint = "/rest/api/3/issuetype";

        JiraInfoDTO found = 지라연결_서비스.checkInfo(connectId);
        WebClient webClient = 지라유틸.클라우드_통신기_생성(found.getUri(), found.getUserId(), found.getPasswordOrToken());

        List<CloudJiraIssueTypeDTO> issueTypes = 지라유틸.get(webClient, endpoint,
                                                new ParameterizedTypeReference<List<CloudJiraIssueTypeDTO>>() {}).block();

        logger.info(issueTypes.toString());

        return issueTypes;
    }

    @Override
    public List<CloudJiraIssueTypeDTO> getIssueTypeListByProjectId(Long connectId, String projectId) throws Exception {

        String endpoint = "/rest/api/3/issuetype/project?projectId=" + projectId;

        JiraInfoDTO found = 지라연결_서비스.checkInfo(connectId);
        WebClient webClient = 지라유틸.클라우드_통신기_생성(found.getUri(), found.getUserId(), found.getPasswordOrToken());

        List<CloudJiraIssueTypeDTO> issueTypes = 지라유틸.get(webClient, endpoint,
                                        new ParameterizedTypeReference<List<CloudJiraIssueTypeDTO>>() {}).block();

        logger.info(issueTypes.toString());

        return issueTypes;
    }

    @Transactional
    @Override
    public CloudJiraIssueTypeDTO createIssueType(Long connectId,
                                                 CloudJiraIssueTypeInputDTO cloudJiraIssueTypeInputDTO)
            throws Exception {

        String endpoint = "/rest/api/3/issuetype";

        JiraInfoDTO found = 지라연결_서비스.checkInfo(connectId);
        WebClient webClient = 지라유틸.클라우드_통신기_생성(found.getUri(), found.getUserId(), found.getPasswordOrToken());

        CloudJiraIssueTypeDTO addCloudJirarIssueTypeDTO = 지라유틸.post(webClient, endpoint,
                cloudJiraIssueTypeInputDTO, CloudJiraIssueTypeDTO.class).block();

        modelMapper.getConfiguration().setSkipNullEnabled(true);
        JiraInfoEntity jiraInfoEntity = modelMapper.map(found, JiraInfoEntity.class);

        if (jiraInfoEntity != null) {
            jiraInfoEntity.setIssueId(addCloudJirarIssueTypeDTO.getId());
            jiraInfoEntity.setIssueName(addCloudJirarIssueTypeDTO.getName());
            jiraInfoEntity.setSelf(addCloudJirarIssueTypeDTO.getSelf());
        }

        JiraInfoEntity returnEntity = 지라연결_서비스.saveIssueTypeInfo(jiraInfoEntity);

        if (returnEntity == null) {
            return null;
        }

        logger.info(addCloudJirarIssueTypeDTO.toString());

        return addCloudJirarIssueTypeDTO;
    }

}
