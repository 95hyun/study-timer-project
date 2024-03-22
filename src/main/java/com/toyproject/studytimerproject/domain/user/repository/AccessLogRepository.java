package com.toyproject.studytimerproject.domain.user.repository;

import com.toyproject.studytimerproject.domain.user.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
