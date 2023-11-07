package com.arms.serverinfo.service;

import com.arms.errors.codes.에러코드;
import com.arms.serverinfo.model.서버정보_데이터;
import com.arms.serverinfo.model.서버정보_엔티티;
import com.arms.serverinfo.repositories.서버정보_저장소;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service("서버정보_서비스")
@AllArgsConstructor
public class 서버정보_서비스_구현 implements 서버정보_서비스 {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private 서버정보_저장소 서버정보_저장소;

    @Autowired
    private com.arms.util.external_communicate.백엔드통신기 백엔드통신기;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private final Logger 로그 = LoggerFactory.getLogger(this.getClass());

    @Override
    public 서버정보_엔티티 서버정보_저장_또는_수정(서버정보_데이터 서버정보_데이터) {

        if (서버정보_데이터 == null) {
            throw new IllegalArgumentException(에러코드.서버정보_오류.getErrorMsg());
        }
        else if (서버정보_데이터.getConnectId() == null) {
            throw new IllegalArgumentException(에러코드.서버_ID정보_오류.getErrorMsg());
        }
        else if (StringUtils.isBlank(서버정보_데이터.getUri())) {
            throw new IllegalArgumentException(에러코드.서버_URI정보_오류.getErrorMsg());
        }
        else if (StringUtils.isBlank(서버정보_데이터.getUserId())) {
            throw new IllegalArgumentException(에러코드.서버_ID정보_오류.getErrorMsg());
        }
        else if (StringUtils.isBlank(서버정보_데이터.getPasswordOrToken())) {
            throw new IllegalArgumentException(에러코드.서버_PW_또큰_API토큰정보_오류.getErrorMsg());
        }
        else if (StringUtils.isBlank(서버정보_데이터.getType())) {
            throw new IllegalArgumentException(에러코드.서버유형_정보오류.getErrorMsg());
        }

        서버정보_엔티티 서버정보_엔티티 = modelMapper.map(서버정보_데이터, 서버정보_엔티티.class);
        서버정보_엔티티 결과 = 서버정보_저장소.save(서버정보_엔티티);

        if (결과 == null) {
            throw new IllegalArgumentException(에러코드.서버정보_생성_오류.getErrorMsg());
        }

        return 결과;
    }

    @Override
    public Iterable<서버정보_엔티티> 서버정보백업_스케줄러() {

        boolean isIndex = isIndexExists(서버정보_엔티티.class);

        if (!isIndex) {
            throw new IllegalArgumentException(에러코드.서버인덱스_NULL_오류.getErrorMsg());
        }

        ResponseEntity<?> 결과 = 백엔드통신기.지라서버정보_가져오기();

        HashMap<String, Object> 서버정보맵 = (HashMap<String, Object>) 결과.getBody();

        if ((boolean) 서버정보맵.get("success") != true) {
            throw new IllegalArgumentException(에러코드.서버정보_조회_오류.getErrorMsg());
        }

        List<LinkedHashMap> 서버정보목록 = (List<LinkedHashMap>) 서버정보맵.get("response");

        List<서버정보_엔티티> result = null;
        if (서버정보목록 != null && !서버정보목록.isEmpty()) {
            result = 서버정보목록.stream()
                    .map(서버정보 -> {
                        서버정보_엔티티 서버정보_엔티티 = new 서버정보_엔티티();
                        서버정보_엔티티.setUri((String) 서버정보.get("c_jira_server_base_url"));
                        서버정보_엔티티.setType((String) 서버정보.get("c_jira_server_type"));
                        서버정보_엔티티.setUserId((String) 서버정보.get("c_jira_server_connect_id"));
                        서버정보_엔티티.setPasswordOrToken((String) 서버정보.get("c_jira_server_connect_pw"));
                        서버정보_엔티티.setConnectId(Long.parseLong((String) 서버정보.get("c_jira_server_etc")));

                        return 서버정보_엔티티;
                    })
                    .collect(Collectors.toList());
        }

        return 서버정보_저장소.saveAll(result);
    }

    public boolean isIndexExists(Class<?> clazz) {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(clazz);

        boolean 결과 = false;

        if (indexOperations.exists()) {
            결과 = indexOperations.exists();
        }
        else {
            결과 = indexOperations.create();
            로그.info("Created index: " + clazz.getSimpleName().toLowerCase());
        }

        return 결과;
    }

//    @Override
//    public 서버정보_엔티티 서버정보_삭제하기(서버정보_데이터 서버정보_데이터) {
//
//        서버정보_데이터 이슈 = 서버정보_검증(서버정보_데이터.getConnectId());
//        서버정보_엔티티 서버정보 = modelMapper.map(이슈, 서버정보_엔티티.class);
//
//        if (이슈 == null) {
//            return null;
//        } else {
//            서버정보_저장소.delete(서버정보);
//            return 서버정보;
//        }
//    }

//    @Override
//    public void 서버정보_전체_삭제하기() {
//        서버정보_저장소.deleteAll();
//    }

    @Override
    public 서버정보_데이터 서버정보_검증(Long 서버_아이디) {

        서버정보_데이터 조회한_서버정보 = 서버정보_조회(서버_아이디);

        if (조회한_서버정보 == null) {
            로그.error("등록된 서버 정보가 아닙니다.");
            throw new IllegalArgumentException(에러코드.서버정보_오류.getErrorMsg());
        }
        else if (조회한_서버정보.getUri() == null || 조회한_서버정보.getUri().isEmpty()) {
            throw new IllegalArgumentException(에러코드.서버_URI정보_오류.getErrorMsg());
        }
        else if (조회한_서버정보.getUserId() == null || 조회한_서버정보.getUserId().isEmpty()) {
            로그.error("사용자 아이디 조회에 실패했습니다.");
            throw new IllegalArgumentException(에러코드.서버_ID정보_오류.getErrorMsg());
        }
        else if (조회한_서버정보.getPasswordOrToken()== null || 조회한_서버정보.getPasswordOrToken().isEmpty()) {
            로그.info("비밀 번호 및 토큰 정보 조회에 실패했습니다.");
            throw new IllegalArgumentException(에러코드.서버_PW_또큰_API토큰정보_오류.getErrorMsg());
        }

        return 조회한_서버정보;
    }

    private 서버정보_데이터 서버정보_조회(Long 서버_아이디) {

        Optional<서버정보_엔티티> optionalEntity = Optional.ofNullable(서버정보_저장소.findById(서버_아이디).orElse(null));

        if (!optionalEntity.isPresent()) {
            return null;
        }

        서버정보_엔티티 서버정보_엔티티 = optionalEntity.get();
        서버정보_데이터 서버정보_데이터 = modelMapper.map(서버정보_엔티티, 서버정보_데이터.class);

        return 서버정보_데이터;
    }
}


