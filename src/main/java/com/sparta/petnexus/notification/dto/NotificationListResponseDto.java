package com.sparta.petnexus.notification.dto;

import com.sparta.petnexus.notification.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "알람 목록 조회 응답 DTO")
public class NotificationListResponseDto {
    private List<NotificationResponseDto> chatList;

    public static NotificationListResponseDto of(List<Notification> notifications) {
        return NotificationListResponseDto.builder()
            .chatList(notifications.stream().map(NotificationResponseDto::of)
                .toList())
            .build();
    }
}
