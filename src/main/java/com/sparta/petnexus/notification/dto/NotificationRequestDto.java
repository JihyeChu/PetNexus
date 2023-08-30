package com.sparta.petnexus.notification.dto;

import com.sparta.petnexus.notification.entity.Notification;
import com.sparta.petnexus.notification.entity.NotificationType;
import com.sparta.petnexus.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class NotificationRequestDto {

    private String url;
    private String content;
    private User receiver;
    private NotificationType notificationType;

    public Notification toEntity(NotificationType notificationType, User receiver) {
        return Notification.builder()
            .url(this.url)
            .content(this.content)
            .notificationType(notificationType)
            .receiver(receiver)
            .isRead(false)
            .build();
    }


}