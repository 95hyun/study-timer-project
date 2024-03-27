package com.toyproject.studytimerproject.domain.study.service;

import com.toyproject.studytimerproject.domain.study.dto.StudyDetailResponseDto;
import com.toyproject.studytimerproject.domain.study.dto.StudyMemberDetailResponseDto;
import com.toyproject.studytimerproject.domain.study.dto.StudyRequestDto;
import com.toyproject.studytimerproject.domain.study.entity.Study;
import com.toyproject.studytimerproject.domain.study.entity.StudyMember;
import com.toyproject.studytimerproject.domain.study.entity.StudyMemberRole;
import com.toyproject.studytimerproject.domain.study.entity.StudySession;
import com.toyproject.studytimerproject.domain.study.repository.StudyMemberRepository;
import com.toyproject.studytimerproject.domain.study.repository.StudyRepository;
import com.toyproject.studytimerproject.domain.study.repository.StudySessionRepository;
import com.toyproject.studytimerproject.domain.user.entity.User;
import com.toyproject.studytimerproject.domain.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;
    private final StudySessionRepository studySessionRepository;


    // 스터디 생성
    @Transactional
    public Study createStudy(StudyRequestDto requestDto, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("User not found");
        }

        log.info("스터디 생성 시작" + userDetails.getUsername());
        // 스터디 생성
        Study study = Study.builder()
                .studyName(requestDto.getStudyName())
                .build();
        Study createStudy = studyRepository.save(study);

        // 스터디 마스터로 사용자 등록 및 저장
        StudyMember studyMember = StudyMember.builder()
                .user(userDetails.getUser())
                .study(createStudy)
                .role(StudyMemberRole.MASTER)
                .build();
        studyMemberRepository.save(studyMember);

        return createStudy;
    }

    // UserId로 StudyId 찾기
    public Long getStudyIdByUser(User user) {
        Long studyId = studyMemberRepository.findStudyIdByUserId(user.getId());
        return studyId;
    }

    // 스터디 상세 페이지 정보 조회 (대시보드)
    public StudyDetailResponseDto getStudyDetails(Long studyId) {
        // 현재 주의 각 요일에 대한 시작 시간(새벽 5시 기준)을 계산합니다.
        Map<String, LocalDateTime> weekStartEndTimes = calculateWeekStartEndTimes();

        // 스터디 ID로 스터디 멤버를 조회합니다.
        List<StudyMember> members = studyMemberRepository.findByStudyId(studyId);
        List<StudyMemberDetailResponseDto> memberDetails = new ArrayList<>();

        // 스터디의 각 멤버에 대해 반복 처리합니다.
        for (StudyMember member : members) {
            StudyMemberDetailResponseDto memberDetail = new StudyMemberDetailResponseDto();
            memberDetail.setNickname(member.getUser().getNickname()); // 멤버의 닉네임 설정

            // 멤버별 일일 학습 시간을 집계합니다.
            weekStartEndTimes.forEach((day, startEndTime) -> {
                LocalDateTime startOfStudyDay = startEndTime; // 해당 날짜의 새벽 5시 시작 시간
                LocalDateTime endOfStudyDay = startOfStudyDay.plusDays(1); // 다음 날의 새벽 5시 종료 시간

                // 멤버의 학습 세션을 시작 시간 기준으로 조회합니다.
                List<StudySession> sessions = studySessionRepository.findByUserIdAndStartTimeBetween(
                        member.getUser().getId(), startOfStudyDay, endOfStudyDay);
                long totalMinutes = sessions.stream()
                        .mapToLong(session -> {
                            // 세션의 종료 시간이 다음 날 새벽 5시를 넘는 경우, 종료 시간을 새벽 5시로 조정합니다.
                            LocalDateTime sessionEnd = session.getEndTime().isAfter(endOfStudyDay) ? endOfStudyDay : session.getEndTime();
                            // 세션의 시작 시간부터 조정된 종료 시간까지의 총 분을 계산합니다.
                            return Duration.between(session.getStartTime(), sessionEnd).toMinutes();
                        })
                        .sum();
                // 해당 요일의 총 학습 시간을 시:분 형식으로 멤버 디테일에 설정합니다.
                memberDetail.setDayHours(day, String.format("%02d:%02d", totalMinutes / 60, totalMinutes % 60));
            });

            memberDetails.add(memberDetail);
        }

        // 스터디 디테일 DTO를 생성하고, 계산된 멤버 디테일 리스트를 설정합니다.
        StudyDetailResponseDto studyDetail = new StudyDetailResponseDto();
        studyDetail.setMembers(memberDetails);

        return studyDetail;
    }

    private Map<String, LocalDateTime> calculateWeekStartEndTimes() {
        LocalDate now = LocalDate.now();
        Map<String, LocalDateTime> weekStartEndTimes = new LinkedHashMap<>();
        // 이번 주 월요일을 기준으로 새벽 5시를 시작 시간으로 설정합니다.
        LocalDate monday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        for (DayOfWeek day : DayOfWeek.values()) {
            LocalDate date = monday.with(TemporalAdjusters.nextOrSame(day));
            // 각 요일에 대해 새벽 5시를 시작 시간으로 하는 LocalDateTime 객체를 생성합니다.
            LocalDateTime startOfStudyDay = date.atTime(5, 0);
            weekStartEndTimes.put(day.getDisplayName(TextStyle.FULL, Locale.getDefault()), startOfStudyDay);
        }
        return weekStartEndTimes;
    }
}
