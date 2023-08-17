package com.sparta.petnexus.trade.dto;

import com.sparta.petnexus.trade.entity.CategoryEnum;
import com.sparta.petnexus.trade.entity.Trade;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TradeResponseDto {

    private String title;
    private String content;
    private String address;
    private int price;
    private CategoryEnum category;

    public static TradeResponseDto of(Trade trade){
        return TradeResponseDto.builder()
                .title(trade.getTitle())
                .content(trade.getContent())
                .address(trade.getAddress())
                .price(trade.getPrice())
                .category(trade.getCategory())
                .build();
    }

}
