package com.toyproject.studytimerproject.domain.study.repository;

import com.toyproject.studytimerproject.domain.study.entity.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
//    List<StudySession> findByUserIdAndDate(Long id, LocalDate value);

    List<StudySession> findByUserIdAndStartTimeBetween(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

}
