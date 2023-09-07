package com.sparta.petnexus.trade.dto;

import com.sparta.petnexus.trade.entity.CategoryEnum;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "trade 생성, 수정 DTO")
public class TradeRequestDto {

    @Schema(description = "trade 제목")
    private String title;
    @Schema(description = "trade 내용")
    private String content;
    @Schema(description = "trade 위도")
    private String latitude;
    @Schema(description = "trade 경도")
    private String longitude;
    @Schema(description = "trade 가격")
    private int price;
    @Schema(description = "trade 품목")
    private CategoryEnum category;

    public Trade toEntity(User user) {
        return Trade.builder()
                .title(this.title)
                .content(this.content)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .price(this.price)
                .category(this.category)
                .user(user)
                .build();
    }
}
