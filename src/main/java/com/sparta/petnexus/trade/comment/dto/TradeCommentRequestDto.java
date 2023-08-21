package com.sparta.petnexus.trade.comment.dto;

import com.sparta.petnexus.trade.comment.entity.TradeComment;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TradeCommentRequestDto {

    private String comment;

    public TradeComment toEntity(User user, Trade trade){
        return TradeComment.builder()
                .comment(this.comment)
                .user(user)
                .trade(trade)
                .build();
    }

}
