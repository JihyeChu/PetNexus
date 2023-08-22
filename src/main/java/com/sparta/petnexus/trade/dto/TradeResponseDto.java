package com.sparta.petnexus.trade.dto;

import com.sparta.petnexus.trade.bookmark.entity.TradeBookmark;
import com.sparta.petnexus.trade.comment.dto.TradeCommentResponseDto;
import com.sparta.petnexus.trade.comment.entity.TradeComment;
import com.sparta.petnexus.trade.entity.CategoryEnum;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.trade.like.entity.TradeLike;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@Schema(description = "trade 응답 DTO")
public class TradeResponseDto {

    @Schema(description = "trade 제목")
    private String title;
    @Schema(description = "trade 내용")
    private String content;
    @Schema(description = "trade 거래희망주소")
    private String address;
    @Schema(description = "trade 가격")
    private int price;
    @Schema(description = "trade 품목")
    private CategoryEnum category;
    @Schema(description = "trade 좋아요 수")
    private int tradeLikeCount;
    @Schema(description = "trade 북마크 리스트")
    private List<String> tradeBookmarkList;
    @Schema(description = "trade 댓글 리스트")
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
