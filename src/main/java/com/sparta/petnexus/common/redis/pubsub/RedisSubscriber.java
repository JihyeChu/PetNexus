package com.sparta.petnexus.common.redis.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.petnexus.chat.dto.ChatMessageDto;
import com.sparta.petnexus.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * 여러 서버에서 SSE 를 구현하기 위한 Redis Pub/Sub
     * Redis 메세지가 발행(publish)되면 대기하고 있던 onMessage 가 해당 메세지를 받아 처리
     * subscribe 해두었던 topic 에 publish 가 일어나면 메서드가 호출된다.
     */
    @Override
    public void onMessage(Message message,byte[] pattern) {
        log.info("Redis Pub/Sub message received: {}", message.toString());
        try{
            // redis 에서 발행된 데이터를 받아 deserialize
            String publishMessage = (String)redisTemplate.getStringSerializer().deserialize(message.getBody());
            log.info(publishMessage);
            // ChatRoom 객체로 매핑
            ChatMessageDto chatMessageDto = objectMapper.readValue(publishMessage, ChatMessageDto.class);
            //ChatRoom roomMessage = objectMapper.readValue(publishMessage, ChatRoom.class);
             log.info("Room - Message : {}", chatMessageDto.getRoomId());
            // WebSocket 구독자에게 채팅 메세지 Send
            messagingTemplate.convertAndSend("/sub/chat/" + chatMessageDto.getRoomId(), chatMessageDto);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}