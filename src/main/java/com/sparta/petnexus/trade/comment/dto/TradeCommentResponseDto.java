package com.sparta.petnexus.trade.comment.dto;

import com.sparta.petnexus.trade.comment.entity.TradeComment;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TradeCommentResponseDto {

    private String comment;

    public static TradeCommentResponseDto of(TradeComment tradeComment){
        return TradeCommentResponseDto.builder()
                .comment(tradeComment.getComment())
                .build();
    }
}
