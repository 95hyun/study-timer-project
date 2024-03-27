package com.toyproject.studytimerproject.domain.study.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class StudyDetailResponseDto {
    private Map<String, LocalDate> dates = new HashMap<>();
    private List<StudyMemberDetailResponseDto> members = new ArrayList<>();
}


