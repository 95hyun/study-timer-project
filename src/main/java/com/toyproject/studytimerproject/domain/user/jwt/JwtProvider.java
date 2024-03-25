package com.toyproject.studytimerproject.domain.user.jwt;

import com.toyproject.studytimerproject.domain.user.entity.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtProvider {
    // Header KEY 값
    public static final String ACCESS_TOKEN_HEADER = "AccessToken";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // Access 토큰 만료시간
    private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    // Refresh 토큰 만료시간
    private final long REFRESH_TOKEN_TIME = 60 * 60 * 24 * 7 * 1000L; // 7일

    @Value("${jwt.secret.key}")
    private String secretKey; // Base64 Encode: Secret Key
    private SecretKey key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * 토큰 생성을 위한 정보 인스턴스 생성
     * @param username : 사용자 고유 식별값 (아이디)
     * @param tokenType : ACCESS | REFRESH
     * @return TokenPayload
     */
    public TokenPayload createTokenPayload(String username, TokenType tokenType) {
        Date date = new Date();
        long tokenTime = TokenType.ACCESS.equals(tokenType) ? ACCESS_TOKEN_TIME : REFRESH_TOKEN_TIME;
        return new TokenPayload(
                username,
                UUID.randomUUID().toString(),
                date,
                new Date(date.getTime() + tokenTime)
        );
    }

    /**
     * 토큰 생성
     * @param payload : 토큰 생성을 위한 정보 인스턴스
     * @return JWT 토큰
     */
    public String createToken(TokenPayload payload) {
        String accessToken = BEARER_PREFIX +
                Jwts.builder()
                        .subject(payload.getSub()) // 사용자 식별자값(ID)
                        .expiration(payload.getExpiresAt()) // 만료 시간
                        .issuedAt(payload.getIat()) // 발급일
                        .id(payload.getJti()) // JWT ID
                        .signWith(key, signatureAlgorithm) // 암호화 Key & 알고리즘
                        .compact();
        return accessToken;
    }

    /**
     * HTTP Header 에서 JWT 추출
     * @param request : HTTP Request 정보
     * @return Header 에서 추출한 JWT
     */
    public String getJwtFromHeader(HttpServletRequest request, TokenType tokenType) {
        String bearerToken = request.getHeader(TokenType.ACCESS.equals(tokenType) ? ACCESS_TOKEN_HEADER : REFRESH_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 토큰의 유효성 검사
     * @param token : JWT
     * @return boolean
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid Token: " + e.getMessage());
        }
        return false;
    }

    /**
     * JWT 내부에 저장한 subject, expiration, issuedAt, id(jti) 정보를 담은 Claims 추출
     * @param token : JWT
     * @return Claims
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

}
