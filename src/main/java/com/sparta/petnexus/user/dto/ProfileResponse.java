package com.sparta.petnexus.user.dto;

import com.sparta.petnexus.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "프로필 수정 DTO")
public class ProfileResponse {

    @Schema(description = "닉네임")
    String nickname;

    String email;

    public ProfileResponse(User user) {

        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }
}
