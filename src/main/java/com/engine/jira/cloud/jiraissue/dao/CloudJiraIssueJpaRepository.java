package com.engine.jira.cloud.jiraissue.dao;

import com.engine.jira.cloud.jiraissue.model.CloudJiraIssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CloudJiraIssueJpaRepository extends JpaRepository<CloudJiraIssueEntity, String> {
}
