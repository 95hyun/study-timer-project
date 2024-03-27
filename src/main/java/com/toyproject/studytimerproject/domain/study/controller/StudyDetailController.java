package com.toyproject.studytimerproject.domain.study.controller;

import com.toyproject.studytimerproject.domain.study.dto.StudyDetailResponseDto;
import com.toyproject.studytimerproject.domain.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyDetailController {

    private final StudyService studyService;

    // 스터디 상세 정보 조회 (대시보드)
    @GetMapping("/{studyId}")
    public String getStudyDetails(@PathVariable Long studyId, Model model) {
        StudyDetailResponseDto studyDetails = studyService.getStudyDetails(studyId);
        model.addAttribute("studyDetails", studyDetails);
        model.addAttribute("dates", studyDetails.getDates()); // 주의 요일별 날짜 정보도 모델에 추가

        return "studyDetail"; // Thymeleaf 템플릿 이름
    }
}
