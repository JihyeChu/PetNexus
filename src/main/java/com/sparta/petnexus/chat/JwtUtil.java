package com.sparta.myblog.jwt;

import com.sparta.myblog.entity.UserRoleEnum;
import com.sparta.myblog.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
@Getter
public class JwtUtil {

    private final RefreshTokenRepository refreshTokenRepository;
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    //private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분
    private final long TOKEN_TIME = 60 * 60 * 1000L * 24 * 7; // 일주일

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JwtAuthenticationFilter
    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(username) // 사용자 식별자값(ID)
                .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    // 생성된 JWT 를 Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            // Cookie Value 에는 공백이 불가능해서 encoding 진행
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

            // Cookie 만들기
            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            // 다 만들어서 HttpServletResponse 객체에 담아준다. -> 클라이언트로 자연스럽게 반환이 된다.
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }

    // JwtAuthorizationFilter
    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    // Cookie 에서 JWT 를 가지고 있는 Cookie 가지고 오는 코드
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // 받아온 Cookie 의 Value 인 JWT 토큰 substring
    // Bearer 과 공백 추가로 붙여 줬던 것 떼어내기 위해
    public String substringToken(String tokenValue) {
        // 공백, null 을 확인하고 startsWith 을 사용하여 토큰의 시작값이 Bearer 이 맞는지 확인
        // 토큰은 무조건 Bearer 로 시작하기 때문에
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            // Bearer 에 공백까지 잘라줌 -> 순수한 Token 값만 나옴
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

  /*  // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        // bearer 붙은 토큰 값 가져온다.
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        log.info(bearerToken);
        // 순수한 토큰을 한번에 Header 에서 뽑아온다.
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }*/

    // 토큰 검증
    public boolean validateToken(String accessToken) throws ExpiredJwtException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return true;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}