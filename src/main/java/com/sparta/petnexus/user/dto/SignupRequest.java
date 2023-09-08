package com.sparta.petnexus.user.dto;

import com.sparta.petnexus.common.security.info.ProviderType;
import com.sparta.petnexus.user.entity.User;
import com.sparta.petnexus.user.entity.UserRoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "회원가입 DTO")
public class SignupRequest {

    @Schema(description = "아이디", example = "email 형식입니다.")
    @NotNull(message = "email 필수 입니다.")
    @Pattern(regexp = "[a-zA-z0-9]+@[a-zA-z]+[.]+[a-zA-z.]+", message = "email 형식이 맞지 않습니다.")
    private String email;

    @Schema(description = "비밀번호", example = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)만 입력 가능합니다.")
    @NotNull(message = "password 필수 입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()]*$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)만 입력 가능합니다.")
    @Size(min = 7, max = 16)
    private String password;

    @Schema(description = "닉네임")
    private String username;

    // username 미작성시, 임의 "user"로 저장
    private String usernameNotNull(String username) {
        if (username == null) {
            username = "user";
        }
        return username;
    }

    public User toEntity(String encodePassword) {
        return User.builder()
                .email(this.email)
                .password(encodePassword)
                .username(usernameNotNull(this.username))
                .nickname("유저")
                .role(UserRoleEnum.USER)
                .providerType(ProviderType.LOCAL)
                .build();
    }
}
