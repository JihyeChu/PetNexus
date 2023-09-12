package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "오픈채팅방 생성 및 수정 요청 DTO")
public class ChatRoomRequestDto {

    @Schema(description = "오픈채팅방 제목", example = "강아지 병원 정보 공유합니다.")
    private String title;
    @Schema(description = "오픈채팅방 설명", example = "서울 인천 경기 지역 분들을 위한 방입니다.")
    private String content;

    public ChatRoom toEntity(User user) {
        return ChatRoom.builder()
            .title(this.title)
            .content(this.content)
            .user(user)
            .build();
    }
}