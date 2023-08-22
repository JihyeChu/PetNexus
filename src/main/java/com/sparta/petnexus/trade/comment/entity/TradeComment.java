package com.sparta.petnexus.trade.comment.entity;

import com.sparta.petnexus.trade.comment.dto.TradeCommentRequestDto;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name="trade_comment")
public class TradeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="trade_id")
    private Trade trade;


    public void update(TradeCommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }
}
