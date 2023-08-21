package com.sparta.petnexus.token.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.jwt.TokenProvider;
import com.sparta.petnexus.token.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<ApiResponse> createNewAccessToken(
            HttpServletRequest request, HttpServletResponse httpResponse) {

        String newAccessToken = tokenService.createNewAccessToken(request);

        httpResponse.addHeader(TokenProvider.HEADER_AUTHORIZATION, newAccessToken);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("AccessToken 재발행 완료", HttpStatus.CREATED.value()));
    }

}
