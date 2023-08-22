package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomRequestDto {
    private Long masterId;
    private String name;

    public ChatRoom toEntity(User user) {
        return ChatRoom.builder()
            .name(name)
            .masterId(user.getId())
            .build();
    }
}