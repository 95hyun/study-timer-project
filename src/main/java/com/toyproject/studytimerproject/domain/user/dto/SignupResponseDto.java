package com.toyproject.studytimerproject.domain.user.dto;

import lombok.Getter;

@Getter
public class SignupResponseDto {
    private Long id;
    private String username;
    private String password;
    private String nickname;

    public SignupResponseDto(Long id, String username, String password, String nickname) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}
