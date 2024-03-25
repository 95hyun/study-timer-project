package com.toyproject.studytimerproject.domain.study.controller;

import com.toyproject.studytimerproject.domain.study.entity.StudySession;
import com.toyproject.studytimerproject.domain.study.service.StudySessionService;
import com.toyproject.studytimerproject.domain.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/study/session")
@RequiredArgsConstructor
public class StudySessionController {
    private final StudySessionService studySessionService;

    // 학습시간 기록 시작
    @PostMapping("/start/{studyId}")
    public ResponseEntity<StudySession> startStudySession(@PathVariable Long studyId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StudySession studySession = studySessionService.startStudySession(studyId, userDetails);
        return ResponseEntity.ok(studySession);
    }

    // 학습시간 기록 종료
    @PostMapping("/end/{sessionId}")
    public ResponseEntity<StudySession> endStudySession(@PathVariable Long sessionId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        StudySession studySession = studySessionService.endStudySession(sessionId, userDetails);
        return ResponseEntity.ok(studySession);
    }
}