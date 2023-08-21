package com.sparta.petnexus.trade.like.entity;

import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "trade_like")
public class TradeLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "trade_id")
    private Trade trade;

    public TradeLike(User user, Trade trade){
        this.user = user;
        this.trade = trade;
    }

}
