package com.toyproject.studytimerproject.domain.study.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class StudyMemberDetailResponseDto {
    private String nickname;
    private Map<String, String> dayHours = new HashMap<>();

    // 요일별 학습 시간을 설정하는 메서드
    public void setDayHours(String day, String hours) {
        this.dayHours.put(day, hours);
    }
}
