package com.toyproject.studytimerproject.domain.user.repository;

import com.toyproject.studytimerproject.domain.user.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
}
