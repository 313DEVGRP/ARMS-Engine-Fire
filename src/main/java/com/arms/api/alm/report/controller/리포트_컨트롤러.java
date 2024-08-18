package com.arms.api.alm.report.controller;

import com.arms.api.alm.report.service.리포트_서비스;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine/jira/dashboard")
@Slf4j
public class 리포트_컨트롤러 {

    @Autowired
    private 리포트_서비스 리포트_서비스;

    @GetMapping("/resourceList")
    public ResponseEntity<?> 작업자_목록_가져오기(Long pdServiceId, Long[] pdServiceVersions) {

        return ResponseEntity.ok(리포트_서비스.작업자_정보_목록_가져오기(pdServiceId, pdServiceVersions, ""));
    }
}
