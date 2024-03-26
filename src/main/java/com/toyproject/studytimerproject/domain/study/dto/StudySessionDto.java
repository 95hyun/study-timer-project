package com.toyproject.studytimerproject.domain.study.dto;

import com.toyproject.studytimerproject.domain.study.entity.StudySession;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StudySessionDto {
    private Long id;
    private Long studyId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public StudySessionDto(Long id, Long studyId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.studyId = studyId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // 정적 팩토리 메서드
    public static StudySessionDto from(StudySession studySession) {
        return new StudySessionDto(studySession.getId(),
                studySession.getStudy().getId(),
                studySession.getStartTime(),
                studySession.getEndTime());
    }
}
