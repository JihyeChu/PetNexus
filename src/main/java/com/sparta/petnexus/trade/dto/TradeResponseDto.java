package com.sparta.petnexus.trade.dto;

import com.sparta.petnexus.trade.entity.CategoryEnum;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.trade.like.entity.TradeLike;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class TradeResponseDto {

    private String title;
    private String content;
    private String address;
    private int price;
    private CategoryEnum category;
    private int tradeLikeCount;

    public static TradeResponseDto of(Trade trade){
        return TradeResponseDto.builder()
                .title(trade.getTitle())
                .content(trade.getContent())
                .address(trade.getAddress())
                .price(trade.getPrice())
                .category(trade.getCategory())
                .tradeLikeCount(trade.getTradeLikes().size())
                .build();
    }

}
