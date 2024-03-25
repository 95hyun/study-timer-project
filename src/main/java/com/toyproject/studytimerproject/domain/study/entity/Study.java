package com.toyproject.studytimerproject.domain.study.entity;

import com.toyproject.studytimerproject.domain.studyMember.entity.StudyMember;
import com.toyproject.studytimerproject.domain.studySession.entity.StudySession;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudyMember> studyMembers = new HashSet<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StudySession> studySessions = new HashSet<>();

    // 랭킹

}
