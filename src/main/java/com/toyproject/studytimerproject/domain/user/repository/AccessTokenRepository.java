package com.toyproject.studytimerproject.domain.user.repository;

import com.toyproject.studytimerproject.domain.user.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findByUserId(Long id);
}
