package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "채팅방 목록 조회 응답 DTO")
public class ChatRoomListResponseDto {

    List<ChatRoomResponseDto> chatRoomList;

    public static ChatRoomListResponseDto of(List<ChatRoom> chats) {
        List<ChatRoomResponseDto> chatRoomResponseDtoList = chats.stream().map(ChatRoomResponseDto::of)
            .toList();
        return ChatRoomListResponseDto.builder()
            .chatRoomList(chatRoomResponseDtoList)
            .build();
    }
}