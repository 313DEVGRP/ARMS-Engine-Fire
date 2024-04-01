package com.arms.api.engine.util;

import com.arms.api.index_entity.이슈_인덱스;
import com.arms.api.alm.issue.model.지라이슈_데이터;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class 지라이슈_생성 {

    public static 이슈_인덱스 ELK_데이터로_변환(
            Long 지라서버_아이디, 지라이슈_데이터 지라이슈_데이터,
            boolean 요구사항유형_여부, String 부모_요구사항_키,
            Long 제품서비스_아이디, Long[] 제품서비스_버전들,
            Long cReqLink
    ) {

        이슈_인덱스.프로젝트 프로젝트 = Optional.ofNullable(지라이슈_데이터.getFields().getProject())
                .map(project -> 이슈_인덱스.프로젝트.builder()
                        .id(project.getId())
                        .key(project.getKey())
                        .name(project.getName())
                        .self(project.getSelf())
                        .build())
                .orElse(null);

        이슈_인덱스.이슈유형 이슈유형 = Optional.ofNullable(지라이슈_데이터.getFields().getIssuetype())
                .map(issuetype -> 이슈_인덱스.이슈유형.builder()
                        .self(issuetype.getSelf())
                        .id(issuetype.getId())
                        .description(issuetype.getDescription())
                        .name(issuetype.getName())
                        .subtask(issuetype.getSubtask())
                        .untranslatedName(issuetype.getUntranslatedName())
                        .hierarchyLevel(issuetype.getHierarchyLevel())
                        .build())
                .orElse(null);

        이슈_인덱스.생성자 생성자 = Optional.ofNullable(지라이슈_데이터.getFields().getCreator())
                .map(creator -> 이슈_인덱스.생성자.builder()
                        .accountId(creator.getAccountId())
                        .emailAddress(creator.getEmailAddress())
                        .displayName(creator.getDisplayName())
                        .build())
                .orElse(null);

        이슈_인덱스.보고자 보고자 = Optional.ofNullable(지라이슈_데이터.getFields().getReporter())
                .map(reporter -> 이슈_인덱스.보고자.builder()
                        .accountId(reporter.getAccountId())
                        .emailAddress(reporter.getEmailAddress())
                        .displayName(reporter.getDisplayName())
                        .build())
                .orElse(null);

        이슈_인덱스.담당자 담당자 = Optional.ofNullable(지라이슈_데이터.getFields().getAssignee())
                .map(assignee -> 이슈_인덱스.담당자.builder()
                        .accountId(assignee.getAccountId())
                        .emailAddress(assignee.getEmailAddress())
                        .displayName(assignee.getDisplayName())
                        .build())
                .orElse(null);

        이슈_인덱스.우선순위 우선순위 = Optional.ofNullable(지라이슈_데이터.getFields().getPriority())
                .map(priority -> 이슈_인덱스.우선순위.builder()
                        .self(priority.getSelf())
                        .id(priority.getId())
                        .name(priority.getName())
                        .description(priority.getDescription())
                        .isDefault(Optional.of(priority.isDefault()).orElse(false)) // 기본값을 false로 설정
                        .build())
                .orElse(null);

        이슈_인덱스.상태 상태 = Optional.ofNullable(지라이슈_데이터.getFields().getStatus())
                .map(status -> 이슈_인덱스.상태.builder()
                        .self(status.getSelf())
                        .id(status.getId())
                        .name(status.getName())
                        .description(status.getDescription())
                        .build())
                .orElse(null);

        이슈_인덱스.해결책 해결책 = Optional.ofNullable(지라이슈_데이터.getFields().getResolution())
                .map(resolution -> 이슈_인덱스.해결책.builder()
                        .self(resolution.getSelf())
                        .id(resolution.getId())
                        .name(resolution.getName())
                        .description(resolution.getDescription())
                        .isDefault(Optional.of(resolution.isDefault()).orElse(false)) // 기본값을 false로 설정
                        .build())
                .orElse(null);

        List<이슈_인덱스.워크로그> 워크로그 = Optional.ofNullable(지라이슈_데이터.getFields().getWorklogs())
                .orElse(Collections.emptyList()) // null인 경우 빈 리스트 반환
                .stream()
                .map(워크로그아이템 -> {
                    이슈_인덱스.저자 저자 = Optional.ofNullable(워크로그아이템.getAuthor())
                            .map(author -> new 이슈_인덱스.저자(
                                    author.getAccountId(),
                                    author.getEmailAddress()))
                            .orElse(null);

                    이슈_인덱스.수정한_저자 수정한_저자 = Optional.ofNullable(워크로그아이템.getUpdateAuthor())
                            .map(updateAuthor -> new 이슈_인덱스.수정한_저자(
                                    updateAuthor.getAccountId(),
                                    updateAuthor.getEmailAddress()))
                            .orElse(null);

                    return new 이슈_인덱스.워크로그(워크로그아이템.getSelf(),
                            저자,
                            수정한_저자,
                            워크로그아이템.getCreated(),
                            워크로그아이템.getUpdated(),
                            워크로그아이템.getStarted(),
                            워크로그아이템.getTimeSpent(),
                            워크로그아이템.getTimeSpentSeconds(),
                            워크로그아이템.getId(),
                            워크로그아이템.getIssueId()
                    );
                })
                .collect(toList());

        이슈_인덱스 이슈 = 이슈_인덱스.builder()
                .jira_server_id(지라서버_아이디)
                .issueID(지라이슈_데이터.getId())
                .key(지라이슈_데이터.getKey())
                .self(지라이슈_데이터.getSelf())
                .parentReqKey(부모_요구사항_키)
                .isReq(요구사항유형_여부)
                .project(프로젝트)
                .issuetype(이슈유형)
                .creator(생성자)
                .reporter(보고자)
                .assignee(담당자)
                .labels(지라이슈_데이터.getFields().getLabels())
                .priority(우선순위)
                .status(상태)
                .resolution(해결책)
                .resolutiondate(지라이슈_데이터.getFields().getResolutiondate())
                .created(지라이슈_데이터.getFields().getCreated())
                .updated(지라이슈_데이터.getFields().getUpdated())
                .worklogs(워크로그)
                .timespent(지라이슈_데이터.getFields().getTimespent())
                .summary(지라이슈_데이터.getFields().getSummary())
                .pdServiceId(제품서비스_아이디)
                .pdServiceVersions(제품서비스_버전들)
                .cReqLink(cReqLink)
                .build();

        이슈.generateId();

        return 이슈;
    }
}
