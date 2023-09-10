package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.Chat;
import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.chat.entity.ChatType;
import com.sparta.petnexus.chat.entity.TradeChat;
import com.sparta.petnexus.chat.entity.TradeChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private ChatType type; // 메시지 타입
    private String roomId;
    private String sender; // 메시지 보낸사람
    private String message;

    public Chat toEntity(ChatRoom chatRoom) { // 오픈채팅
        return Chat.builder()
            .chatRoom(chatRoom)
            .sender(this.sender)
            .message(this.message)
            .type(this.type)
            .build();
    }

    public TradeChat toEntity(TradeChatRoom tradeChatRoom) { // 중고거래 채팅
        return TradeChat.builder()
            .tradeChatRoom(tradeChatRoom)
            .sender(this.sender)
            .message(this.message)
            .build();
    }
}