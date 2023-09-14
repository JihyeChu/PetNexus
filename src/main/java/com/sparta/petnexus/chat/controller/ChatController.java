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
    // /pub/chat/enter 에 메세지가 오면 동작
    @MessageMapping("chat/enter/{roomId}")
    @SendTo("/sub/enter/{roomId}")
    public ChatMessageDto enter(@DestinationVariable Long roomId,
        ChatMessageDto messageDto) { // 채팅방 입장

        return ChatMessageDto.builder()
            .roomId(roomId)
            .sender(messageDto.getSender())
            .message(messageDto.getSender() + "님이 채팅방에 참여하였습니다.")
            .build();
    }
    // /pub/chat/message 에 메세지가 오면 동작
    @MessageMapping("chat/message/{roomId}") // 오픈채팅
    @SendTo("/sub/chat/{roomId}")
    public ChatMessageDto message(@DestinationVariable Long roomId, ChatMessageDto messageDto) {
        chatService.saveMessage(roomId, messageDto);

        return ChatMessageDto.builder()
            .roomId(roomId)
            .sender(messageDto.getSender())
            .message(messageDto.getMessage())
            .build();
    }

    // /pub/trade-chat/message 에 메세지가 오면 동작
    @MessageMapping("tradechat/message/{roomId}") // 중고거래 채팅
    @SendTo("/sub/tradechat/{roomId}")
    public ChatMessageDto tradeMessage(@DestinationVariable Long roomId,
        ChatMessageDto messageDto) {
        chatService.saveTradeMessage(roomId, messageDto);
        return ChatMessageDto.builder()
            .roomId(roomId)
            .sender(messageDto.getSender())
            .message(messageDto.getMessage())
            .build();
    }
}