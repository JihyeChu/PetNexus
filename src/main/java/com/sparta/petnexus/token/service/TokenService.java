package com.sparta.petnexus.token.service;


import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.common.security.jwt.TokenProvider;
import com.sparta.petnexus.token.entity.RefreshToken;
import com.sparta.petnexus.token.repository.RefreshTokenRepository;
import com.sparta.petnexus.user.entity.User;
import com.sparta.petnexus.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createNewAccessToken(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(TokenProvider.REFRESH_TOKEN_COOKIE_NAME);
        String refreshToken = tokenProvider.getAccessToken(authorizationHeader);

        if (!tokenProvider.validToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        RefreshToken token = refreshTokenRepository.findByRefreshToken(authorizationHeader)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_REFRESH_TOKEN));

        User user = userService.findById(token.getId());

        return tokenProvider.generateToken(user, TokenProvider.ACCESS_TOKEN_DURATION);
    }
}