package com.sparta.petnexus.user.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.user.dto.SignUpUserRequest;
import com.sparta.petnexus.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signUp(SignUpUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EXISTED_EMAIL);
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());
        userRepository.save(request.toEntity(encodePassword));
    }
}
