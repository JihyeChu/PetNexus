package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Schema(description = "오픈채팅방 조회 응답 DTO")
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponseDto {

    @Schema(description = "오픈채팅방 id")
    private String id;
    @Schema(description = "오픈채팅방 이름", example = "강아지 병원 정보 공유합니다.")
    private String title;
    @Schema(description = "오픈채팅방 설명", example = "서울 인천 경기 지역 분들을 위한 방입니다.")
    private String content;
    @Schema(description = "사용자 이름", example = "won1122")
    private String username;

    public static ChatRoomResponseDto of(ChatRoom chatRoom) {
        return ChatRoomResponseDto.builder()
            .id(chatRoom.getId())
            .title(chatRoom.getTitle())
            .content(chatRoom.getContent())
            .username(chatRoom.getUser().getUsername())
            .build();
    }
}