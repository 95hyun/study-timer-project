package com.toyproject.studytimerproject.domain.study.repository;

import com.toyproject.studytimerproject.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {
}
