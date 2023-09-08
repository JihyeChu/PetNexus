package com.sparta.petnexus.notification.dto;

import com.sparta.petnexus.notification.entity.Notification;
import com.sparta.petnexus.notification.entity.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "알람 조회 응답 DTO")
public class NotificationResponseDto {

    @Schema(description = "알람 아이디", example = "1")
    private Long id;
    @Schema(description = "알람 내용")
    private String content;
    @Schema(description = "알람 타입 (like/comment/purchase)")
    private NotificationType type;
    @Schema(description = "중고 거래 상품 페이지 url")
    private String url;
    @Schema(description = "알람 읽음 여부")
    private boolean isRead;
    @Schema(description = "알람 생성 일자")
    private LocalDateTime createdAt;

    public static NotificationResponseDto of(Notification notification) {
        return NotificationResponseDto.builder()
            .id(notification.getId())
            .content(notification.getContent())
            .type(notification.getNotificationType())
            .createdAt(notification.getCreatedAt())
            .url(notification.getUrl())
            .isRead(false)
            .build();
    }
}