package com.toyproject.studytimerproject.domain.study.controller;

import com.toyproject.studytimerproject.domain.study.dto.StudyDetailResponseDto;
import com.toyproject.studytimerproject.domain.study.dto.StudyRequestDto;
import com.toyproject.studytimerproject.domain.study.entity.Study;
import com.toyproject.studytimerproject.domain.study.service.StudyService;
import com.toyproject.studytimerproject.domain.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study")
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


    // 유저가 스터디에 속해있는지 체크
    @GetMapping("/check-study-membership")
    public ResponseEntity<?> checkStudyMembership(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long studyId = studyService.getStudyIdByUser(userDetails.getUser());
        if (studyId != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("isMember", true);
            response.put("studyId", studyId);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(Collections.singletonMap("isMember", false));
        }
    }
}
