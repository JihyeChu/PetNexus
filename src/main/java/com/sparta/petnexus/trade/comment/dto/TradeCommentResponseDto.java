package com.sparta.petnexus.trade.comment.dto;

import com.sparta.petnexus.trade.comment.entity.TradeComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "trade 댓글 응답 DTO")
public class TradeCommentResponseDto {

    @Schema(description = "trade 품목")
    private String comment;

    public static TradeCommentResponseDto of(TradeComment tradeComment){
        return TradeCommentResponseDto.builder()
                .comment(tradeComment.getComment())
                .build();
    }
}
