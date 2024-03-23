package com.toyproject.studytimerproject.domain.study.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;

@Entity
@Getter
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String studyName;

    @Column
    private String studyPassword;

    // 랭킹

}
