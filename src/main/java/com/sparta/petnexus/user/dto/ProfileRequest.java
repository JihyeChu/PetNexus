package com.sparta.petnexus.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "프로필 수정 DTO")
public class ProfileRequest {

    @Schema(description = "닉네임")
    String nickname;

}
