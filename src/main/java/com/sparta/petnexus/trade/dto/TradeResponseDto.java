package com.sparta.petnexus.trade.dto;

import com.sparta.petnexus.Image.entity.Image;
import com.sparta.petnexus.trade.comment.dto.TradeCommentResponseDto;
import com.sparta.petnexus.trade.entity.Trade;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "trade 응답 DTO")
public class TradeResponseDto {

    @Schema(description = "trade id")
    private Long id;
    @Schema(description = "trade 제목")
    private String title;
    @Schema(description = "trade 내용")
    private String content;
    @Schema(description = "trade 작성자")
    private String username;
    @Schema(description = "trade 위도")
    private String latitude;
    @Schema(description = "trade 경도")
    private String longitude;
    @Schema(description = "trade 가격")
    private String price;
    @Schema(description = "trade 품목")
    private String category;
    @Schema(description = "trade 좋아요 수")
    private int tradeLikeCount;
    @Schema(description = "trade 북마크 리스트")
    private List<String> tradeBookmarkList;
    @Schema(description = "trade 댓글 리스트")
    private List<TradeCommentResponseDto> tradeCommentList;
    @Schema(description = "trade에 달린 이미지 리스트")
    private List<String> imageList;
    @Schema(description = "trade 챗팅방")
    private Long tradeChatroom;


    public static TradeResponseDto of(Trade trade) {
        DecimalFormat df = new DecimalFormat("###,###,###");

        return TradeResponseDto.builder()
                .id(trade.getId())
                .title(trade.getTitle())
                .content(trade.getContent())
                .username(trade.getUser().getUsername())
                .latitude(trade.getLatitude())
                .longitude(trade.getLongitude())
                .price(df.format(trade.getPrice()))
                .category(String.valueOf(trade.getCategory()))
                .tradeLikeCount(trade.getTradeLikes().size())
                .tradeBookmarkList(trade.getTradeBookmarks().stream().map(bookmark -> bookmark.getTrade().getTitle()).toList())
                .tradeCommentList(trade.getTradeComments().stream().map(TradeCommentResponseDto::of).toList())
                .imageList(trade.getImage().stream().map(Image::getImageUrl).toList())
                .tradeChatroom(trade.getTradeChatRoom().getId())
                .build();
    }

}