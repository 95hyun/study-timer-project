package com.toyproject.studytimerproject.domain.user.service;

import com.toyproject.studytimerproject.domain.user.entity.TokenType;
import com.toyproject.studytimerproject.domain.user.entity.User;
import com.toyproject.studytimerproject.domain.user.jwt.JwtProvider;
import com.toyproject.studytimerproject.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public String refreshAccessToken(String refreshToken) {
        // refreshToken 유효성 검사
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid Token");
        }
        // Jwt Claims
        Claims info = jwtProvider.getUserInfoFromToken(refreshToken);

        // User 조회
        User user = userRepository.findByUsername(info.getSubject()).orElseThrow(
                () -> new RuntimeException("Not Found User By : " + info.getSubject())
        );

        return jwtProvider.createToken(jwtProvider.createTokenPayload(user.getUsername(), TokenType.ACCESS));
    }
}
