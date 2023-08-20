package com.sparta.petnexus.trade.entity;

import com.sparta.petnexus.trade.bookmark.entity.TradeBookmark;
import com.sparta.petnexus.trade.dto.TradeRequestDto;
import com.sparta.petnexus.trade.like.entity.TradeLike;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name="trade")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String address;

    @Column
    private int price;

    @Column
    private CategoryEnum category;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "trade", cascade = CascadeType.REMOVE)
    private List<TradeLike> tradeLikes = new ArrayList<>();

    @OneToMany(mappedBy = "trade", cascade = CascadeType.REMOVE)
    private List<TradeBookmark> tradeBookmarks = new ArrayList<>();

    public void update(TradeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.address = requestDto.getAddress();
        this.category = requestDto.getCategory();
    }
}
