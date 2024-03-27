package com.toyproject.studytimerproject.domain.study.repository;

import com.toyproject.studytimerproject.domain.study.entity.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Long> {
    @Query("SELECT sm.study.id FROM StudyMember sm WHERE sm.user.id = :userId")
    Long findStudyIdByUserId(@Param("userId") Long userId);

    List<StudyMember> findByStudyId(Long studyId);
}
