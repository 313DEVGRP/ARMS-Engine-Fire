package com.arms.cloud.jiraissue.service;

import com.arms.cloud.jiraissue.domain.CloudJiraIssueDTO;
import com.arms.cloud.jiraissue.domain.CloudJiraIssueInputDTO;
import com.arms.cloud.jiraissue.domain.CloudJiraIssueSearchDTO;
import com.arms.config.CloudJiraConfig;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
@Service("cloudJiraIssue")
public class CloudJiraIssueImpl implements CloudJiraIssue {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("cloudJiraConfig")
    private CloudJiraConfig cloudJiraConfig;

    @Override
    public CloudJiraIssueSearchDTO getIssueSearch(String projectKeyOrId) {
        final WebClient jiraWebClient = cloudJiraConfig.getJiraWebClient();

        String endpoint = "/rest/api/3/search?jql=project=" + projectKeyOrId;

        CloudJiraIssueSearchDTO response = jiraWebClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(CloudJiraIssueSearchDTO.class)
                .block();

        String jsonResponse = response.toString();
        logger.info(jsonResponse);

        return response;
    }

    @Override
    public CloudJiraIssueDTO getIssue(String issueKeyOrId) {
        final WebClient jiraWebClient = cloudJiraConfig.getJiraWebClient();

        String endpoint = "/rest/api/3/issue/" + issueKeyOrId;

        CloudJiraIssueDTO response = jiraWebClient.get()
                .uri(endpoint)
                .retrieve()
                .bodyToMono(CloudJiraIssueDTO.class)
                .block();

        String jsonResponse = response.toString();
        logger.info(jsonResponse);

        return response;
    }

    @Override
    public CloudJiraIssueDTO createIssue(CloudJiraIssueInputDTO cloudJiraIssueInputDTO) throws Exception {

        final WebClient jiraWebClient = cloudJiraConfig.getJiraWebClient();

        String endpoint = "/rest/api/3/issue";

        CloudJiraIssueDTO response = jiraWebClient.post()
                .uri(endpoint)
                .bodyValue(cloudJiraIssueInputDTO)
                .retrieve()
                .bodyToMono(CloudJiraIssueDTO.class)
                .block();

        String jsonResponse = response.toString();
        logger.info(jsonResponse);

        return response;
    }

}
