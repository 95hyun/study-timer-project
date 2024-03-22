package com.toyproject.studytimerproject.domain.user.controller;

import com.toyproject.studytimerproject.domain.user.dto.SignupRequestDto;
import com.toyproject.studytimerproject.domain.user.dto.SignupResponseDto;
import com.toyproject.studytimerproject.domain.user.entity.TokenType;
import com.toyproject.studytimerproject.domain.user.jwt.JwtProvider;
import com.toyproject.studytimerproject.domain.user.service.AuthService;
import com.toyproject.studytimerproject.domain.user.service.TokenBlackListService;
import com.toyproject.studytimerproject.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final UserService userService;
    private final TokenBlackListService tokenBlackListService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        return ResponseEntity.ok(userService.signup(requestDto));
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        tokenBlackListService.addToBlackList(
                jwtProvider.getJwtFromHeader(request, TokenType.ACCESS),
                jwtProvider.getJwtFromHeader(request, TokenType.REFRESH)
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request) {
        String accessToken = authService.refreshAccessToken(jwtProvider.getJwtFromHeader(request, TokenType.REFRESH));
        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/blacklist/reset")
    public ResponseEntity<Void> resetBlackList() {
        tokenBlackListService.removeExpiredTokens();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
