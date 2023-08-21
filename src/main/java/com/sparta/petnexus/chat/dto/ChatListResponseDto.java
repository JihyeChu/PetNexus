package com.sparta.petnexus.chat.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class ChatListResponseDto {

    private List<ChatResponseDto> chatList;

    public ChatListResponseDto(List<ChatResponseDto> chatList) {
        this.chatList = chatList;
    }
}