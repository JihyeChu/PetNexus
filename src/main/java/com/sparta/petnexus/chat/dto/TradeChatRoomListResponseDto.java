package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "중고거래 채팅방 목록 조회 응답 DTO")
public class TradeChatRoomListResponseDto {

    List<TradeChatRoomResponseDto> tradeChatRoomList;

    public static TradeChatRoomListResponseDto of(List<ChatRoom> chats) {
        List<TradeChatRoomResponseDto> tradeChatRoomResponseDtoList = chats.stream()
            .map(TradeChatRoomResponseDto::of)
            .toList();
        return TradeChatRoomListResponseDto.builder()
            .tradeChatRoomList(tradeChatRoomResponseDtoList)
            .build();
    }
}