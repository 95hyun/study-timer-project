package com.toyproject.studytimerproject.domain.study.entity;

import com.toyproject.studytimerproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudyDailySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = true) // 스터디 참여가 선택적임
    private Study study;


    private LocalDate date;

    private Duration totalStudyTime;

    public void addStudyTime(Duration studyTime) {
        if (totalStudyTime == null) {
            totalStudyTime = Duration.ZERO;
        }
        this.totalStudyTime = this.totalStudyTime.plus(studyTime);
    }
}
