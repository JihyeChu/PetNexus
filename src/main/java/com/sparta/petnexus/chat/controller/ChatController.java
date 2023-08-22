package com.sparta.petnexus.chat.controller;

import com.sparta.petnexus.chat.dto.ChatRequestDto;
import com.sparta.petnexus.chat.dto.ChatResponseDto;
import com.sparta.petnexus.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final SimpMessagingTemplate template; //특정 Broker 로 메세지를 전달
    private final ChatService chatService;

    // stompConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합됨 (/pub + ...)
    // /pub/chat/enter 에 메세지가 오면 동작
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatRequestDto message) { // 채팅방 입장
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/" + message.getRoomId(), message);
    }

    // /pub/chat/message 에 메세지가 오면 동작
    @MessageMapping(value = "/chat/message")
    public void message(ChatRequestDto message) {
        ChatResponseDto savedMessage = chatService.saveMessage(message);
        template.convertAndSend("/sub/chat/" + savedMessage.getRoomId(), savedMessage);
    }
}