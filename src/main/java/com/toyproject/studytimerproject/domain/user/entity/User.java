package com.toyproject.studytimerproject.domain.user.entity;

import com.toyproject.studytimerproject.domain.study.entity.StudyMember;
import com.toyproject.studytimerproject.domain.study.entity.StudySession;
import com.toyproject.studytimerproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "아이디는 필수입니다")
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull(message = "비밀번호는 필수입니다")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "닉네임은 필수입니다")
    @Column(nullable = false, unique = true)
    private String nickname;

//    @Column
//    @Enumerated(value = EnumType.STRING)
//    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudyMember> studyMembers = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudySession> studySessions = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<AccessToken> accessTokens;

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "user")
    private List<AccessLog> accessLogs;

    public User(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
//        this.role = UserRole.MEMBER;
    }
}
