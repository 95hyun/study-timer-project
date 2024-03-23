package com.toyproject.studytimerproject.domain.studySession.entity;

import com.toyproject.studytimerproject.domain.study.entity.Study;
import com.toyproject.studytimerproject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class StudySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Study study;
}
