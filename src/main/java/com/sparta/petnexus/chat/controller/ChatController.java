package com.sparta.petnexus.chat.controller;

import com.sparta.petnexus.chat.dto.ChatMessageDto;
import com.sparta.petnexus.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    // stompConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합됨 (/pub + ...)
    // /pub/chat/message 에 메세지가 오면 동작
    // 채팅방에 발행된 메시지는 서로 다른 서버에 공유하기 위해 redis 의 Topic 으로 발행
    @MessageMapping("chat/message/{roomId}") // 오픈채팅
    public ChatMessageDto message(@DestinationVariable String roomId, ChatMessageDto messageDto) {
        chatService.sendChatMessage(roomId, messageDto);

        return ChatMessageDto.builder()
            .roomId(roomId)
            .sender(messageDto.getSender())
            .message(messageDto.getMessage())
            .build();
    }

    // /pub/trade-chat/message 에 메세지가 오면 동작
    @MessageMapping("tradechat/message/{roomId}") // 중고거래 채팅
    @SendTo("/sub/tradechat/{roomId}")
    public ChatMessageDto tradeMessage(@DestinationVariable String roomId,
        ChatMessageDto messageDto) {

        chatService.saveTradeMessage(roomId, messageDto);

        return ChatMessageDto.builder()
            .roomId(roomId)
            .sender(messageDto.getSender())
            .message(messageDto.getMessage())
            .build();
    }
}