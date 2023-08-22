package com.sparta.petnexus.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "회원가입 DTO")
public class LoginRequest {

    @Schema(description = "아이디", example = "email 형식입니다.")
    String email;

    @Schema(description = "비밀번호", example = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)만 입력 가능합니다.")
    String password;
}
