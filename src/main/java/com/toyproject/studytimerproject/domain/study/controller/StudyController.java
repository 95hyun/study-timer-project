package com.toyproject.studytimerproject.domain.study.controller;

import com.toyproject.studytimerproject.domain.study.dto.StudyRequestDto;
import com.toyproject.studytimerproject.domain.study.entity.Study;
import com.toyproject.studytimerproject.domain.study.service.StudyService;
import com.toyproject.studytimerproject.domain.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {

    private final StudyService studyService;

    // 스터디 생성
    @PostMapping
    public ResponseEntity<Study> createStudy(@RequestBody StudyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Study study = studyService.createStudy(requestDto, userDetails);
        return ResponseEntity.ok(study);
    }

    // 스터디 참여

    // 스터디 삭제

    // 스터디 조회
}
