package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "채팅방 조회 응답 DTO")
public class ChatRoomResponseDto {
    @Schema(description = "채팅방 이름", example = "강아지 병원 정보 공유합니다.")
    private String name;

    public static ChatRoomResponseDto of(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
            .name(chatRoom.getName())
            .build();
    }
}