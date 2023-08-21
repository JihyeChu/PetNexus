package com.sparta.petnexus.common.security.oauth;


import com.sparta.petnexus.common.security.info.OAuth2UserInfo;
import com.sparta.petnexus.common.security.info.OAuth2UserInfoFactory;
import com.sparta.petnexus.common.security.info.ProviderType;
import com.sparta.petnexus.common.security.jwt.TokenProvider;
import com.sparta.petnexus.common.util.CookieUtil;
import com.sparta.petnexus.user.entity.User;
import com.sparta.petnexus.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/";

    private final TokenProvider tokenProvider;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        ProviderType providerType = ProviderType.valueOf(
                authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType,
                oAuth2User.getAttributes());
        User user = userRepository.findByEmail((String) userInfo.getAttributes().get("email"));

        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String refreshToken = tokenProvider.generateRefreshToken(user, REFRESH_TOKEN_DURATION);

        //jwt토큰 생성 및 패스에 토큰 추가
        String token = URLEncoder.encode(accessToken, "utf-8").replaceAll("\\+", "%20");

        // accesstoken -> cookie
        addAccessToCookie(request, response, token);

        response.addHeader(TokenProvider.REFRESH_TOKEN_COOKIE_NAME, refreshToken);

        clearAuthenticationAttributes(request, response);

//        String targetUrl = getTargetUrl(accessToken);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void addAccessToCookie(HttpServletRequest request, HttpServletResponse response,
            String accessToken) {
        int cookieMaxAge = (int) TokenProvider.ACCESS_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, TokenProvider.HEADER_AUTHORIZATION);
        CookieUtil.addCookie(response, TokenProvider.HEADER_AUTHORIZATION, accessToken,
                cookieMaxAge);
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response,
            String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request,
            HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}