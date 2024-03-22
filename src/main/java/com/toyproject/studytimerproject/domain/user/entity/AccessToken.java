package com.toyproject.studytimerproject.domain.user.entity;

import com.toyproject.studytimerproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
public class AccessToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String jti;

    @Column
    private String token;

    @Column
    private Date expiresAt;

    @Column(columnDefinition = "boolean default false")
    private boolean isRevoke;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
