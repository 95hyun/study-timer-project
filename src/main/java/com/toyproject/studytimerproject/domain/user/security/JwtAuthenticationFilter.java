package com.toyproject.studytimerproject.domain.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.studytimerproject.domain.user.dto.LoginRequestDto;
import com.toyproject.studytimerproject.domain.user.entity.*;
import com.toyproject.studytimerproject.domain.user.jwt.JwtProvider;
import com.toyproject.studytimerproject.domain.user.jwt.TokenPayload;
import com.toyproject.studytimerproject.domain.user.repository.AccessLogRepository;
import com.toyproject.studytimerproject.domain.user.repository.AccessTokenRepository;
import com.toyproject.studytimerproject.domain.user.repository.RefreshTokenRepository;
import com.toyproject.studytimerproject.global.util.HttpRequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;
    private final AccessLogRepository accessLogRepository;

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, AccessLogRepository accessLogRepository, AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository) {
        this.jwtProvider = jwtProvider;
        this.accessLogRepository = accessLogRepository;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Login 성공
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        String username = user.getUsername();

//        // 토큰 생성
//        String accessToken = jwtProvider.createToken(jwtProvider.createTokenPayload(username, TokenType.ACCESS));
//        String refreshToken = jwtProvider.createToken(jwtProvider.createTokenPayload(username, TokenType.REFRESH));

        // 액세스 토큰 페이로드 생성
        TokenPayload accessTokenPayload = jwtProvider.createTokenPayload(user.getUsername(), TokenType.ACCESS);
        String accessTokenValue = jwtProvider.createToken(accessTokenPayload);

        // 리프레시 토큰 페이로드 생성
        TokenPayload refreshTokenPayload = jwtProvider.createTokenPayload(user.getUsername(), TokenType.REFRESH);
        String refreshTokenValue = jwtProvider.createToken(refreshTokenPayload);

        // AccessToken 엔티티 생성 및 저장
        AccessToken accessToken = AccessToken.builder()
                .token(accessTokenValue)
                .jti(accessTokenPayload.getJti())
                .expiresAt(accessTokenPayload.getExpiresAt())
                .user(user)
                .build();
        accessTokenRepository.save(accessToken);

        // RefreshToken 엔티티 생성 및 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .jti(refreshTokenPayload.getJti())
                .expiresAt(refreshTokenPayload.getExpiresAt())
                .user(user)
                .build();
        refreshTokenRepository.save(refreshToken);

        // response 반환
        response.addHeader(JwtProvider.ACCESS_TOKEN_HEADER, accessTokenValue);
        response.addHeader(JwtProvider.REFRESH_TOKEN_HEADER, refreshTokenValue);

        AccessLog accessLog = new AccessLog(
                HttpRequestUtils.getUserAgent(request),
                request.getRequestURI(),
                HttpRequestUtils.getRemoteAddr(request),
                user
        );
        accessLogRepository.save(accessLog);
    }

    /**
     * Login 실패
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
