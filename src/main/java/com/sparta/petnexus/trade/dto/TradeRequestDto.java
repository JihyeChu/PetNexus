package com.sparta.petnexus.trade.dto;

import com.sparta.petnexus.trade.entity.CategoryEnum;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TradeRequestDto {

    private String title;
    private String content;
    private String address;
    private int price;
    private CategoryEnum category;

    public Trade toEntity(User user){
        return Trade.builder()
                .title(this.title)
                .content(this.content)
                .address(this.address)
                .price(this.price)
                .category(this.category)
                .user(user)
                .build();
    }
}
