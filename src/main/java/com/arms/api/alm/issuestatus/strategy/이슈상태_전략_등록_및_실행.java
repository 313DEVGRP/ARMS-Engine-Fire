package com.arms.api.alm.issuestatus.strategy;

import com.arms.api.alm.issuestatus.model.이슈상태_데이터;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class 이슈상태_전략_등록_및_실행 {

    이슈상태_전략 이슈상태_전략;

    public List<이슈상태_데이터> 이슈상태_목록_가져오기(Long 연결_아이디) throws Exception {
        return this.이슈상태_전략.이슈상태_목록_가져오기(연결_아이디);
    }

    public List<이슈상태_데이터> 프로젝트별_이슈상태_목록_가져오기(Long 연결_아이디, String 프로젝트_아이디) throws Exception {
        return this.이슈상태_전략.프로젝트별_이슈상태_목록_가져오기(연결_아이디, 프로젝트_아이디);
    }

    public void 지라이슈상태_전략_등록(이슈상태_전략 이슈상태_전략) throws Exception{

        this.이슈상태_전략 = 이슈상태_전략;
    }
}
