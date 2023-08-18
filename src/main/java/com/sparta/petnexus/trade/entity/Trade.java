package com.sparta.petnexus.trade.entity;

import com.sparta.petnexus.trade.dto.TradeRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
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

//    @Builder.Default
//    @OneToMany(mappedBy = "user", orphanRemoval = true)
//    private List<User> users = new ArrayList<>();

    @Builder
    public Trade(TradeRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.address = requestDto.getAddress();
        this.category = requestDto.getCategory();
    }

    public void update(TradeRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.address = requestDto.getAddress();
        this.category = requestDto.getCategory();
    }
}
