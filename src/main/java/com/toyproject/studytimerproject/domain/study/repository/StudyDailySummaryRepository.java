package com.toyproject.studytimerproject.domain.study.repository;

import com.toyproject.studytimerproject.domain.study.entity.StudyDailySummary;
import com.toyproject.studytimerproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyDailySummaryRepository extends JpaRepository<StudyDailySummary, Long> {
    Optional<StudyDailySummary> findByUserIdAndDate(Long id, LocalDate date);

    List<StudyDailySummary> findByUserAndDateBetween(User user, LocalDate weekStartDate, LocalDate weekEndDate);
}
