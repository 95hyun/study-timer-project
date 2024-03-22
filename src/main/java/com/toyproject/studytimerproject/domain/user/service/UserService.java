package com.toyproject.studytimerproject.domain.user.service;

import com.toyproject.studytimerproject.domain.user.dto.SignupRequestDto;
import com.toyproject.studytimerproject.domain.user.dto.SignupResponseDto;
import com.toyproject.studytimerproject.domain.user.entity.User;
import com.toyproject.studytimerproject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signup(SignupRequestDto requestDto) {
        Optional<User> userByUsername = userRepository.findByUsername(requestDto.getUsername());
        if (userByUsername.isPresent()) {
            throw new RuntimeException(requestDto.getUsername() + "already exists");
        }

        User createUser = new User(
                requestDto.getUsername(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getNickname()
        );
        userRepository.save(createUser);

        return new SignupResponseDto(
                createUser.getId(),
                createUser.getUsername(),
                createUser.getPassword(),
                createUser.getNickname()
        );
    }

}
