package com.toyproject.studytimerproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StudyTimerProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyTimerProjectApplication.class, args);
    }

}
