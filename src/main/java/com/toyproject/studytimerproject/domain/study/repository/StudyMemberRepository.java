package com.toyproject.studytimerproject.domain.study.repository;

import com.toyproject.studytimerproject.domain.study.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
}
