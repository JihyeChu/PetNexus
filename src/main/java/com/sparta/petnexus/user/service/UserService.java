package com.sparta.petnexus.user.service;

import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.common.redis.utils.RedisUtils;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.common.security.jwt.TokenProvider;
import com.sparta.petnexus.common.util.CookieUtil;
import com.sparta.petnexus.user.dto.LoginRequest;
import com.sparta.petnexus.user.dto.SignupRequest;
import com.sparta.petnexus.user.entity.User;
import com.sparta.petnexus.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private final RedisUtils redisUtils;

    public void signUp(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EXISTED_EMAIL);
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());
        userRepository.save(request.toEntity(encodePassword));
    }

    public void logIn(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            LoginRequest request) {
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

            // generateRefreshToken, and redis save -> email, refreshToken, duration
            String refreshToken = tokenProvider.generateRefreshToken(user,
                    TokenProvider.REFRESH_TOKEN_DURATION);

            // accessToken add cookie before encode // if value has a // maybe throw exception.
            String token = URLEncoder.encode(accessToken, "utf-8").replaceAll("\\+", "%20");

            // accesstoken -> cookie
            addAccessToCookie(httpRequest, httpResponse, token);

            //refreshtoken -> header
            httpResponse.addHeader(TokenProvider.REFRESH_TOKEN_COOKIE_NAME, refreshToken);

            // TODO : accessToken redirect param -> front catch param and save in local storage and refreshToken save in cookie
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.BAD_ID_PASSWORD);
        }
    }

    private void addAccessToCookie(HttpServletRequest request, HttpServletResponse response,
            String accessToken) {
        int cookieMaxAge = (int) TokenProvider.ACCESS_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, TokenProvider.HEADER_AUTHORIZATION);
        CookieUtil.addCookie(response, TokenProvider.HEADER_AUTHORIZATION, accessToken,
                cookieMaxAge);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_FOUND_USER)
        );
    }

    public void createNewAccessToken(HttpServletRequest request, HttpServletResponse httpResponse)
            throws UnsupportedEncodingException {
        String tokenValue = tokenProvider.getTokenFromCookie(request);
        String accessToken = tokenProvider.getAccessToken(tokenValue);
        String requestToken = request.getHeader(TokenProvider.REFRESH_TOKEN_COOKIE_NAME);
        String email = tokenProvider.getAuthentication(accessToken).getName();
        String refreshToken = redisUtils.get(email, String.class);

        if(requestToken.equals(refreshToken)) {
            String validToken = tokenProvider.getAccessToken(refreshToken);
            if (!tokenProvider.validToken(validToken)) {
                throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
        }
        User user = userRepository.findByEmail(email);

        tokenProvider.generateToken(user, TokenProvider.ACCESS_TOKEN_DURATION);
        String newToken = tokenProvider.generateToken(user,
                TokenProvider.ACCESS_TOKEN_DURATION);
        String newAccessToken = URLEncoder.encode(newToken, "utf-8").replaceAll("\\+", "%20");

        // accesstoken -> cookie
        addAccessToCookie(request, httpResponse, newAccessToken);
    }

}
