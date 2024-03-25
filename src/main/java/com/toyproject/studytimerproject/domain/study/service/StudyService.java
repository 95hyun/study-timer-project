package com.toyproject.studytimerproject.domain.study.service;

import com.toyproject.studytimerproject.domain.study.dto.StudyRequestDto;
import com.toyproject.studytimerproject.domain.study.entity.Study;
import com.toyproject.studytimerproject.domain.study.entity.StudyMember;
import com.toyproject.studytimerproject.domain.study.entity.StudyMemberRole;
import com.toyproject.studytimerproject.domain.study.repository.StudyMemberRepository;
import com.toyproject.studytimerproject.domain.study.repository.StudyRepository;
import com.toyproject.studytimerproject.domain.user.repository.UserRepository;
import com.toyproject.studytimerproject.domain.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyRepository studyRepository;


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
}
