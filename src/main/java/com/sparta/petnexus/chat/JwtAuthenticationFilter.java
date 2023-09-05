package com.sparta.myblog.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myblog.dto.LoginRequestDto;
import com.sparta.myblog.entity.User;
import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.entity.redishash.RefreshToken;
import com.sparta.myblog.repository.RefreshTokenRepository;
import com.sparta.myblog.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;


    public JwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        setFilterProcessesUrl("/view/user/login");
    }

    //로그인 시도 시 동작하는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            //requestBody 요청 값을 LoginRequestDto 객체타입으로 변환
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            //아이디, 비밀번호 입력내용을 체크
            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword(),
                    null
                )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // 인증 성공(로그인 성공) 시 동작하는 메서드
    // JWT 생성 및 client 에 응답
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        log.info("로그인 성공 및 JWT 생성");
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        String username = user.getUsername();
        UserRoleEnum role = user.getRole();

        String refreshTokenVal = UUID.randomUUID().toString();
        refreshTokenRepository.save(new RefreshToken(refreshTokenVal));
        String token = jwtUtil.createToken(username, role);

        response.addHeader("RefreshToken", refreshTokenVal);
        log.info(refreshTokenVal);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        // addJwtToCookie() - Cookie 생성하고 Response 객체에 넣어주는 메서드
        jwtUtil.addJwtToCookie(token, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }

}