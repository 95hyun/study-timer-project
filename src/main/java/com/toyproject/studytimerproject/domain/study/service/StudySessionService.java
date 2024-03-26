package com.toyproject.studytimerproject.domain.study.service;

import com.toyproject.studytimerproject.domain.study.dto.StudySessionDto;
import com.toyproject.studytimerproject.domain.study.entity.Study;
import com.toyproject.studytimerproject.domain.study.entity.StudyDailySummary;
import com.toyproject.studytimerproject.domain.study.entity.StudySession;
import com.toyproject.studytimerproject.domain.study.repository.StudyDailySummaryRepository;
import com.toyproject.studytimerproject.domain.study.repository.StudyRepository;
import com.toyproject.studytimerproject.domain.study.repository.StudySessionRepository;
import com.toyproject.studytimerproject.domain.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudySessionService {

    private final StudyRepository studyRepository;
    private final StudySessionRepository studySessionRepository;
    private final StudyDailySummaryRepository studyDailySummaryRepository;

    @Transactional
    public StudySessionDto startStudySession(Long studyId, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("User not found");
        }

        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new RuntimeException("Study not found"));

        log.info("학습 기록 시작" + userDetails.getUsername());

        StudySession studySession = StudySession.builder()
                .user(userDetails.getUser())
                .study(study)
                .startTime(LocalDateTime.now())
                .build();
        studySessionRepository.save(studySession);
        return StudySessionDto.from(studySession);
    }

    @Transactional
    public StudySessionDto endStudySession(Long sessionId, UserDetailsImpl userDetails) {
        StudySession studySession = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("StudySession not found"));

        if (!studySession.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new IllegalArgumentException("스터디세션의 유저와 사용자가 일치하지 않습니다.");
        }

        log.info("학습 기록 종료" + userDetails.getUsername());

        studySession.setEndTime(); // 종료 시간 설정
        Duration sessionDuration = Duration.between(studySession.getStartTime(), studySession.getEndTime());

        studySessionRepository.save(studySession);

        // 일 학습 시간 정보 업데이트
        StudyDailySummary dailySummary = studyDailySummaryRepository.findByUserIdAndDate(userDetails.getUser().getId(), LocalDate.now())
                .orElseGet(() -> StudyDailySummary.builder()
                        .user(userDetails.getUser())
                        .date(LocalDate.now())
                        .totalStudyTime(Duration.ZERO)
                        .build());

        dailySummary.addStudyTime(sessionDuration);
        studyDailySummaryRepository.save(dailySummary);

        log.info("학습 기록 종료 - 일 학습 시간 업데이트" + userDetails.getUsername() + " | " + sessionDuration);

        return StudySessionDto.from(studySession);
    }

}
