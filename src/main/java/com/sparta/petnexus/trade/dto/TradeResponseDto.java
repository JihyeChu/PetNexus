package com.sparta.petnexus.trade.dto;

import com.sparta.petnexus.trade.bookmark.entity.TradeBookmark;
import com.sparta.petnexus.trade.comment.dto.TradeCommentResponseDto;
import com.sparta.petnexus.trade.comment.entity.TradeComment;
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
    private List<String> tradeBookmarkList;
    private List<TradeCommentResponseDto> tradeCommentList;

    public static TradeResponseDto of(Trade trade){
        return TradeResponseDto.builder()
                .title(trade.getTitle())
                .content(trade.getContent())
                .address(trade.getAddress())
                .price(trade.getPrice())
                .category(trade.getCategory())
                .tradeLikeCount(trade.getTradeLikes().size())
                .tradeBookmarkList(trade.getTradeBookmarks().stream().map(bookmark -> bookmark.getTrade().getTitle()).toList())
                .tradeCommentList(trade.getTradeComments().stream().map(TradeCommentResponseDto::of).toList())
                .build();
    }

}
