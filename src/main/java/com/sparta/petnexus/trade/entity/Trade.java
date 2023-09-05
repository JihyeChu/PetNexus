package com.sparta.petnexus.trade.entity;

import com.sparta.petnexus.Image.entity.Image;
import com.sparta.petnexus.trade.bookmark.entity.TradeBookmark;
import com.sparta.petnexus.trade.comment.entity.TradeComment;
import com.sparta.petnexus.trade.dto.TradeRequestDto;
import com.sparta.petnexus.trade.like.entity.TradeLike;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.ErrorResponse;

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
    private String latitude;

    @Column
    private String longitude;

    @Column
    private int price;

    @Column
    private CategoryEnum category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "trade")
    private List<TradeLike> tradeLikes = new ArrayList<>();

    @OneToMany(mappedBy = "trade", cascade = CascadeType.REMOVE)
    private List<TradeBookmark> tradeBookmarks = new ArrayList<>();

    @OneToMany(mappedBy = "trade", cascade = CascadeType.REMOVE)
    private List<TradeComment> tradeComments = new ArrayList<>();

    @OneToMany(mappedBy = "trade", cascade = CascadeType.REMOVE)
    private List<Image> image = new ArrayList<>();

    public void update(TradeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.latitude = requestDto.getLatitude();
        this.longitude = requestDto.getLongitude();
        this.category = requestDto.getCategory();
    }
}
