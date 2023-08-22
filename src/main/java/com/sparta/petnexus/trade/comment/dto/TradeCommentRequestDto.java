package com.sparta.petnexus.trade.comment.dto;

import com.sparta.petnexus.trade.comment.entity.TradeComment;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "trade 댓글 DTO")
public class TradeCommentRequestDto {

    @Schema(description = "trade 댓글")
    private String comment;

    public TradeComment toEntity(User user, Trade trade){
        return TradeComment.builder()
                .comment(this.comment)
                .user(user)
                .trade(trade)
                .build();
    }

}
