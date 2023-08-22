package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.Chat;
import com.sparta.petnexus.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ChatRequestDto {
    private Long roomId;
    private Long writerId;
    private String writer; // 채팅을 보낸 사람
    private String message;

    public Chat toEntity(ChatRoom chatRoom) {
        return Chat.builder()
            .chatRoom(chatRoom)
            .writerId(this.writerId)
            .writer(this.writer)
            .message(this.message)
            .build();
    }
}