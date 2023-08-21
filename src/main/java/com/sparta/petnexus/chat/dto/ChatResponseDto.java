package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.Chat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatResponseDto {
    private Long roomId;
    private Long userId;
    private String writer;
    private String message;
    private LocalDateTime createdAt;

    public static ChatResponseDto of(Chat chat) {
        return ChatResponseDto.builder()
            .userId(chat.getUserId())
            .roomId(chat.getRoomId())
            .writer(chat.getWriter())
            .message(chat.getMessage())
            .createdAt(chat.getCreatedAt())
            .build();
    }
}