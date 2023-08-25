package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.TradeChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "중고거래 채팅방 조회 응답 DTO")
public class TradeChatRoomResponseDto {

    @Schema(description = "중고 거래 게시글 ID", example = "1")
    private Long tradeId;
    @Schema(description = "구매자 ID", example = "1")
    private Long buyerId;
    @Schema(description = "판매자 ID", example = "2")
    private Long sellerId;

    public static TradeChatRoomResponseDto of(TradeChatRoom chatRoom) {
        return TradeChatRoomResponseDto.builder()
            .tradeId(chatRoom.getTrade().getId())
            .buyerId(chatRoom.getBuyer().getId())
            .sellerId(chatRoom.getSellerId())
            .build();
    }
}