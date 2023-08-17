package com.sparta.petnexus.user.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.user.dto.SignUpUserRequest;
import com.sparta.petnexus.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignUpUserRequest request) {
        userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("회원가입 완료", HttpStatus.CREATED.value()));
    }
}
