package com.sparta.petnexus.user.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.user.dto.AddPetRequest;
import com.sparta.petnexus.user.dto.LoginRequest;
import com.sparta.petnexus.user.dto.ProfileRequest;
import com.sparta.petnexus.user.dto.SignupRequest;
import com.sparta.petnexus.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "유저 API", description = "유저 관련 API 입니다.")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입", description = "@RequestBody 통해 SignupRequestDto를 받아와 회원가입을 진행합니다.")
    @PostMapping("/user/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody @Valid SignupRequest request) {
        userService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("회원가입 완료", HttpStatus.CREATED.value()));
    }

    @Operation(summary = "로그인", description = "@RequestBody 통해 LoginRequestDto를 받아와 로그인을 진행합니다.")
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse> logIn(HttpServletRequest httpRequest,
            HttpServletResponse httpResponse,
            @RequestBody LoginRequest request) {
        userService.logIn(httpRequest, httpResponse, request);
        return ResponseEntity.ok(new ApiResponse("로그인 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "로그아웃", description = "httpRequest 통해 redis에 저장된 refreshtoken을 삭제하고, Accesstoken을 블랙리스트에 저장합니다.")
    @PostMapping("/user/logout")
    public ResponseEntity<ApiResponse> logOut(HttpServletRequest httpRequest) {
        userService.logOut(httpRequest);
        return ResponseEntity.ok(new ApiResponse("로그아웃", HttpStatus.OK.value()));
    }

    @Operation(summary = "AccessToken 재발급", description = "AccessToken 만료시 RefreshToken을 통해 재발급합니다.")
    @PostMapping("/user/token")
    public ResponseEntity<ApiResponse> createNewAccessToken(
            HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        userService.createNewAccessToken(httpRequest, httpResponse);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("AccessToken 재발행 완료", HttpStatus.CREATED.value()));
    }

    @Operation(summary = "Profile 수정", description = "@RequestBody 통해 ProfileRequestDto를 통해 username을 수정합니다.")
    @PutMapping("/user/profile")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody ProfileRequest request,
            @AuthenticationPrincipal
            UserDetailsImpl userDetails) {
        userService.updateProfile(request, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponse("프로필 수정 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "펫 등록", description = "@RequestBody 통해 AddPetRequestDto를 받아와 펫을 등록합니다.")
    @PostMapping("/profile/pet")
    public ResponseEntity<ApiResponse> addPet(@RequestBody AddPetRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.addPet(request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("펫 생성 완료", HttpStatus.CREATED.value()));
    }

    @Operation(summary = "펫 등록", description = "@RequestBody 통해 AddPetRequestDto를 받아와 펫을 등록합니다.")
    @PutMapping("/profile/pet/{petId}")
    public ResponseEntity<ApiResponse> updatePet(@Parameter(description = "저장한 펫 id", in = ParameterIn.PATH) @PathVariable Long petId,
            @RequestBody AddPetRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.updatePet(petId, request, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponse("펫 수정 완료", HttpStatus.OK.value()));
    }

    @Operation(summary = "펫 삭제", description = "@PathVariable 통해 저장한 펫을 삭제합니다.")
    @DeleteMapping("/profile/pet/{petId}")
    public ResponseEntity<ApiResponse> deletePet(@Parameter(description = "저장한 펫 id", in = ParameterIn.PATH) @PathVariable Long petId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deletePet(petId, userDetails.getUser());
        return ResponseEntity.ok(new ApiResponse("펫 삭제 완료", HttpStatus.OK.value()));
    }

}
