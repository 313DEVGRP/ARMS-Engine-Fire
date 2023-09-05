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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service("서버정보_서비스")
@AllArgsConstructor
public class 서버정보_서비스_구현 implements 서버정보_서비스 {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private 서버정보_저장소 서버정보_저장소;
    private final Logger 로그 = LoggerFactory.getLogger(this.getClass());

    @Override
    public 서버정보_엔티티 서버정보_저장_또는_수정(서버정보_데이터 서버정보_데이터){

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

        if (결과 == null){
            throw new IllegalArgumentException(에러코드.서버정보_생성_오류.getErrorMsg());
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
//    public void 서버정보_전체_삭제하기(){
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


