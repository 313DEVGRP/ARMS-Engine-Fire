package com.arms.api.alm.report.controller;

import com.arms.api.alm.report.model.FullDataRequestDTO;
import com.arms.api.alm.report.service.리포트_서비스;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/engine/report")
@Slf4j
public class 리포트_컨트롤러 {

    @Autowired
    private 리포트_서비스 리포트_서비스;

    @GetMapping("/resource-list")
    public ResponseEntity<?> 작업자_목록_가져오기() {

        return ResponseEntity.ok(리포트_서비스.작업자_목록_가져오기());
    }


    @GetMapping("/issue-list")
    public ResponseEntity<?> 이슈_목록_가져오기(FullDataRequestDTO fullDataRequestDTO) {
        return ResponseEntity.ok(리포트_서비스.pdServiceId_조건으로_이슈_목록_가져오기(fullDataRequestDTO));
    }
}
