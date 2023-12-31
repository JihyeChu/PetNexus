package com.sparta.petnexus.common.security.jwt;


import com.sparta.petnexus.common.redis.utils.RedisUtils;
import com.sparta.petnexus.common.security.entity.UserDetailServiceImp;
import com.sparta.petnexus.common.util.CookieUtil;
import com.sparta.petnexus.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailServiceImp userDetailServiceImp;
    private final RedisUtils redisUtils;

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(7);

    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return BEARER_PREFIX + Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public String generateRefreshToken(User user, Duration expiredAt) {
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiredAt.toMillis()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
//        saveRefreshTokenInRedis(user, refreshToken);
        return refreshToken;
    }

    private void saveRefreshTokenInRedis(User user, String refreshToken) {
        redisUtils.put(user.getEmail(), refreshToken, TokenProvider.REFRESH_TOKEN_DURATION);
    }

    public void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response,
            String refreshToken) {
        int cookieMaxAge = (int) TokenProvider.REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, TokenProvider.REFRESH_TOKEN_COOKIE_NAME);

        CookieUtil.addCookie(response, TokenProvider.REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    public void addTokenToCookie(HttpServletRequest request, HttpServletResponse response,
            String token) throws UnsupportedEncodingException {
        int cookieMaxAge = (int) TokenProvider.ACCESS_TOKEN_DURATION.toSeconds();

        String encodeToken = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20");

        CookieUtil.deleteCookie(request, response, TokenProvider.HEADER_AUTHORIZATION);

        CookieUtil.addCookie(response, TokenProvider.HEADER_AUTHORIZATION, encodeToken, cookieMaxAge);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN_COOKIE_NAME)) {
//                    return cookie.getValue();
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

    public String getTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(HEADER_AUTHORIZATION)) {
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

    public String getTokenStompHeader(String token) {
        try {
            return getAccessToken(URLDecoder.decode(token, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
//            if (redisUtils.hasKeyBlackList(token)){
//                throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
//            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        UserDetails userDetails = userDetailServiceImp.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, token,
                userDetails.getAuthorities());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith(
                TokenProvider.BEARER_PREFIX)) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    public Duration getExpiration(String token) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parser().setSigningKey(jwtProperties.getSecretKey()) .parseClaimsJws(token).getBody().getExpiration();
        // 현재 시간
        Date now = new Date();
        return Duration.ofHours((expiration.getTime() - now.getTime()));
    }

}
