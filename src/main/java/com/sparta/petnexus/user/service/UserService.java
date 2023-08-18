package com.sparta.petnexus.user.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.common.security.jwt.TokenProvider;
import com.sparta.petnexus.token.entity.RefreshToken;
import com.sparta.petnexus.token.repository.RefreshTokenRepository;
import com.sparta.petnexus.user.dto.LoginRequest;
import com.sparta.petnexus.user.dto.SignupRequest;
import com.sparta.petnexus.user.entity.User;
import com.sparta.petnexus.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public void signUp(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EXISTED_EMAIL);
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());
        userRepository.save(request.toEntity(encodePassword));
    }

    public void logIn(HttpServletResponse httpResponse, LoginRequest request) {
        try {
            Authentication authentication = authenticationConfiguration.getAuthenticationManager()
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getPassword(),
                                    null
                            )
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

            String accessToken = tokenProvider.generateToken(user,
                    TokenProvider.ACCESS_TOKEN_DURATION);
            String refreshToken = tokenProvider.generateToken(user,
                    TokenProvider.REFRESH_TOKEN_DURATION);
            saveRefreshToken(user.getId(), refreshToken);

            httpResponse.addHeader(TokenProvider.HEADER_AUTHORIZATION, accessToken);
            httpResponse.addHeader(TokenProvider.REFRESH_TOKEN_COOKIE_NAME, refreshToken);

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.BAD_ID_PASSWORD);
        }
    }

    public void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );
    }
}
