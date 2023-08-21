package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChatRequestDto {
    private Long roomId;
    private Long userId;
    private String writer; // 채팅을 보낸 사람
    private String message;

    public Chat toEntity() {
        return Chat.builder()
            .userId(this.userId)
            .roomId(this.roomId)
            .writer(this.writer)
            .message(this.message)
            .build();
    }
}