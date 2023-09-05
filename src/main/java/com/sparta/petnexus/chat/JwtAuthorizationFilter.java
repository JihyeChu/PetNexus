package com.sparta.petnexus.chat;

import com.sparta.myblog.entity.redishash.RefreshToken;
import com.sparta.myblog.repository.RefreshTokenRepository;
import com.sparta.myblog.security.UserDetailsImpl;
import com.sparta.myblog.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService,
        RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 필터링 작업을 수행하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain) throws ServletException, IOException {

        //String tokenValue = jwtUtil.getJwtFromHeader(req);
        // 임시
        //String tokenValue = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlc2MwMjE4IiwiYXV0aCI6IlVTRVIiLCJleHAiOjE2OTI5MTY4ODksImlhdCI6MTY5MjMxMjA4OX0.VAeqRVroJlvXoFhKul7fdlZG5ZjHM7Qgs_7TntB1Ou8";

        // Cookie 에서 JWT 를 가지고 있는 Cookie 가지고 오는 코드
        String tokenValue = jwtUtil.getTokenFromRequest(req);
        String refreshToken = req.getHeader("RefreshToken");

        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring
            tokenValue = jwtUtil.substringToken(tokenValue);
            log.info(tokenValue);

            boolean isValid = false;
            try {
                isValid = jwtUtil.validateToken(tokenValue);
            } catch (ExpiredJwtException e) {
                log.error("토큰 만료");
                System.out.println(tokenValue);
                RefreshToken refToken = refreshTokenRepository.findById(refreshToken)
                    .orElseThrow(() -> new IllegalArgumentException("리프레시 실패"));
                isValid = true;
                Long id = refToken.getMemberId();
                UserDetails userDetails = userDetailsService.loadUserById(id);

                String refreshTokenVal = UUID.randomUUID().toString();
                refreshTokenRepository.delete(refToken);
                refreshTokenRepository.save(new RefreshToken(refreshTokenVal));
                tokenValue = jwtUtil.createToken(userDetails.getUsername(),
                        ((UserDetailsImpl) userDetails).getRole())
                    .substring(7);
                res.addHeader("RefreshToken", refreshTokenVal);
                res.addHeader(JwtUtil.AUTHORIZATION_HEADER, tokenValue);

            }
            if (!isValid) {
                log.error("Token Error");
                return;
            }
            log.info(tokenValue);

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드를 설정
                res.getWriter().write("Authentication failed!"); // 오류 메시지를 응답 본문에 작성
                return; // 필터 체인의 나머지 처리를 중단하고 응답을 반환
            }
        }
        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) throws UsernameNotFoundException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
    }
}
