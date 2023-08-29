package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.TradeChat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "중고거래 채팅방 내 채팅 목록 조회 응답 DTO")
public class TradeChatListResponseDto {

    private List<TradeChatResponseDto> tradeChatList;

    public static TradeChatListResponseDto of(List<TradeChat> tradeChats) {
        List<TradeChatResponseDto> tradeChatResponseDtoList = tradeChats.stream().map(TradeChatResponseDto::of)
            .toList();
        return TradeChatListResponseDto.builder()
            .tradeChatList(tradeChatResponseDtoList)
            .build();
    }
}