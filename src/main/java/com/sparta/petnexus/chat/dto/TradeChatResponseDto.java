package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.TradeChat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "중고거래 채팅방 채팅 목록 조회 응답 DTO")
public class TradeChatResponseDto {
    @Schema(description = "채팅방 아이디", example = "1")
    private Long roomId;
    @Schema(description = "작성자")
    private String sender;
    @Schema(description = "메세지 내용")
    private String message;
    @Schema(description = "메세지 생성 일자")
    private LocalDateTime createdAt;

    public static TradeChatResponseDto of(TradeChat tradeChat) {
        return TradeChatResponseDto.builder()
            .roomId(tradeChat.getTradeChatRoom().getId())
            .sender(tradeChat.getSender())
            .message(tradeChat.getMessage())
            .createdAt(tradeChat.getCreatedAt())
            .build();
    }
}