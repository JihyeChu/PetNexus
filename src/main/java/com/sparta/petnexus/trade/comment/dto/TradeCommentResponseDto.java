package com.sparta.petnexus.trade.comment.dto;

import com.sparta.petnexus.trade.comment.entity.TradeComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "trade 댓글 응답 DTO")
public class TradeCommentResponseDto {

    @Schema(description = "trade 댓글")
    private String comment;

    @Schema(description = "trade id", example = "1")
    private Long id;

    @Schema(description = "작성자명")
    private String username;

    public static TradeCommentResponseDto of(TradeComment tradeComment){
        return TradeCommentResponseDto.builder()
                .comment(tradeComment.getComment())
                .id(tradeComment.getId())
                .username(tradeComment.getUser().getUsername())
                .build();
    }
}
