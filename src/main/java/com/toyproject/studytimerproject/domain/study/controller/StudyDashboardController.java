package com.toyproject.studytimerproject.domain.study.controller;

import com.toyproject.studytimerproject.domain.study.service.StudySummaryService;
import com.toyproject.studytimerproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/study/dashboard")
public class StudyDashboardController {
    private final StudySummaryService studySummaryService;

    @GetMapping("/{studyId}")
    public String getStudyDashboard(@PathVariable Long studyId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate,
                                    Model model) {
        Map<User, Map<DayOfWeek, Duration>> weeklyStudyTimes = studySummaryService.getWeeklyStudyTimesForStudy(studyId, weekStartDate);

        model.addAttribute("weeklyStudyTimes", weeklyStudyTimes);
        model.addAttribute("weekStartDate", weekStartDate);
        model.addAttribute("studyId", studyId);

        return "studyDashboard"; // Thymeleaf 템플릿 파일명
    }
}
