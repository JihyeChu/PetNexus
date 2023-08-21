package com.sparta.petnexus.trade.bookmark.entity;

import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name="bookmark")
public class TradeBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="trade_id")
    private Trade trade;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public TradeBookmark(User user, Trade trade){
        this.trade = trade;
        this.user = user;
    }

}
