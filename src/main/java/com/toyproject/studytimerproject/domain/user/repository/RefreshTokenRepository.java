package com.toyproject.studytimerproject.domain.user.repository;

import com.toyproject.studytimerproject.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
