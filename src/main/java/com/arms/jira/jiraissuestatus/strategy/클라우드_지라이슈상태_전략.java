package com.arms.jira.jiraissuestatus.strategy;

import com.arms.errors.codes.에러코드;
import com.arms.jira.jiraissuestatus.model.지라이슈상태_데이터;
import com.arms.jira.jiraissuestatus.model.클라우드_지라이슈상태_데이터;
import com.arms.jira.utils.지라유틸;
import com.arms.serverinfo.model.서버정보_데이터;
import com.arms.serverinfo.service.서버정보_서비스;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;


@Component
public class 클라우드_지라이슈상태_전략 implements 지라이슈상태_전략 {

    private final Logger 로그 = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private 서버정보_서비스 서버정보_서비스;


    @Autowired
    private 지라유틸 지라유틸;

    @Override
    public List<지라이슈상태_데이터> 이슈_상태_목록_가져오기(Long 연결_아이디) throws Exception{

        로그.info("클라우드 이슈 상태 목록 가져오기");

        try {
            서버정보_데이터 서버정보 = 서버정보_서비스.서버정보_검증(연결_아이디);
            WebClient webClient = 지라유틸.클라우드_통신기_생성(서버정보.getUri(), 서버정보.getUserId(), 서버정보.getPasswordOrToken());

            int startAt = 0;
            int 최대_검색수 = 지라유틸.최대_검색수_가져오기();
            boolean checkLast = false;

            List<지라이슈상태_데이터> 반환할_지라_이슈_상태_데이터전송객체_목록 = new ArrayList<지라이슈상태_데이터>();

            while(!checkLast) {
                String endpoint = "/rest/api/3/statuses/search?maxResults="+ 최대_검색수 + "&startAt=" + startAt;
                클라우드_지라이슈상태_데이터 지라_이슈_상태_조회_결과 = 지라유틸.get(webClient, endpoint, 클라우드_지라이슈상태_데이터.class).block();

                반환할_지라_이슈_상태_데이터전송객체_목록.addAll(지라_이슈_상태_조회_결과.getValues());

                for (지라이슈상태_데이터 이슈_상태 : 반환할_지라_이슈_상태_데이터전송객체_목록) {
                    String self = 서버정보.getUri() + "/rest/api/3/statuses?id=" + 이슈_상태.getId();
                    이슈_상태.setSelf(self);
                }

                if (지라_이슈_상태_조회_결과.getTotal() == 반환할_지라_이슈_상태_데이터전송객체_목록.size()) {
                    checkLast = true;
                }
                else {
                    startAt += 최대_검색수;
                }
            }

            return 반환할_지라_이슈_상태_데이터전송객체_목록;
        }catch (Exception e){
            로그.error("클라우드 이슈 상태 목록 조회에 실패하였습니다" +e.getMessage());
            throw new IllegalArgumentException(에러코드.이슈상태_조회_오류.getErrorMsg());
        }
    }


    @Override
    public List<지라이슈상태_데이터> 프로젝트별_이슈_상태_목록_가져오기(Long 연결_아이디, String 프로젝트_아이디) throws Exception{

        로그.info("클라우드 프로젝트별_이슈_상태_목록_가져오기 실행");

        if (프로젝트_아이디 == null || 프로젝트_아이디.isEmpty()) {
            throw new IllegalArgumentException(에러코드.파라미터_NULL_오류.getErrorMsg());
        }

        try {
            서버정보_데이터 서버정보 = 서버정보_서비스.서버정보_검증(연결_아이디);
            WebClient webClient = 지라유틸.클라우드_통신기_생성(서버정보.getUri(), 서버정보.getUserId(), 서버정보.getPasswordOrToken());

            int startAt = 0;
            int 최대_검색수 = 지라유틸.최대_검색수_가져오기();
            boolean checkLast = false;

            List<지라이슈상태_데이터> 반환할_지라_이슈_상태_데이터전송객체_목록 = new ArrayList<지라이슈상태_데이터>();

            while(!checkLast) {
                String endpoint = "/rest/api/3/statuses/search?maxResults="+ 최대_검색수 + "&startAt=" + startAt + "&projectId="+프로젝트_아이디;
                클라우드_지라이슈상태_데이터 지라_이슈_상태_조회_결과 = 지라유틸.get(webClient, endpoint, 클라우드_지라이슈상태_데이터.class).block();

                반환할_지라_이슈_상태_데이터전송객체_목록.addAll(지라_이슈_상태_조회_결과.getValues());

                for (지라이슈상태_데이터 이슈_상태 : 반환할_지라_이슈_상태_데이터전송객체_목록) {
                    String self = 서버정보.getUri() + "/rest/api/3/statuses?id=" + 이슈_상태.getId();
                    이슈_상태.setSelf(self);
                }

                if (지라_이슈_상태_조회_결과.getTotal() == 반환할_지라_이슈_상태_데이터전송객체_목록.size()) {
                    checkLast = true;
                }
                else {
                    startAt += 최대_검색수;
                }
            }

            return 반환할_지라_이슈_상태_데이터전송객체_목록;
        }catch (Exception e){
            로그.error("클라우드 이슈 상태 목록 조회에 실패하였습니다" +e.getMessage());
            throw new IllegalArgumentException(에러코드.이슈상태_조회_오류.getErrorMsg());
        }
    }

}
