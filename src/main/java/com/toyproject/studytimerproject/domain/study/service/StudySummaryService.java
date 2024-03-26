package com.toyproject.studytimerproject.domain.study.service;

import com.toyproject.studytimerproject.domain.study.entity.Study;
import com.toyproject.studytimerproject.domain.study.entity.StudyDailySummary;
import com.toyproject.studytimerproject.domain.study.repository.StudyDailySummaryRepository;
import com.toyproject.studytimerproject.domain.study.repository.StudyRepository;
import com.toyproject.studytimerproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudySummaryService {

    private final StudyRepository studyRepository;
    private final StudyDailySummaryRepository studyDailySummaryRepository;

    public Map<User, Map<DayOfWeek, Duration>> getWeeklyStudyTimesForStudy(Long studyId, LocalDate weekStartDate) {
        Study study = studyRepository.findById(studyId)
                .orElseThrow(() -> new IllegalArgumentException("Study not found"));

        LocalDate weekEndDate = weekStartDate.plusDays(6);
        Map<User, Map<DayOfWeek, Duration>> weeklyStudyTimes = new HashMap<>();

        study.getStudyMembers().forEach(studyMember -> {
            User user = studyMember.getUser();
            List<StudyDailySummary> summaries = studyDailySummaryRepository.findByUserAndDateBetween(user, weekStartDate, weekEndDate);

            Map<DayOfWeek, Duration> userWeeklyTimes = summaries.stream()
                    .collect(Collectors.toMap(
                            summary -> summary.getDate().getDayOfWeek(),
                            StudyDailySummary::getTotalStudyTime,
                            Duration::plus));

            weeklyStudyTimes.put(user, userWeeklyTimes);
        });

        return weeklyStudyTimes;
    }

}
