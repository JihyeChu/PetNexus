package com.sparta.petnexus.chat.entity;

import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TradeChatRoom extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyer; // 중고거래 구매자

    @OneToOne
    @JoinColumn(name = "trade_id")
    private Trade trade; // 중고거래 상품

    @OneToMany(mappedBy = "tradeChatRoom", cascade = CascadeType.ALL)
    private List<TradeChat> messageList = new ArrayList<>();
}
