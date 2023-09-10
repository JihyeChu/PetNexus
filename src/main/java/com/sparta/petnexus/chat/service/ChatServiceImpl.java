package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatMessageDto;
import com.sparta.petnexus.chat.dto.TradeChatListResponseDto;
import com.sparta.petnexus.chat.entity.Chat;
import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.chat.entity.TradeChat;
import com.sparta.petnexus.chat.entity.TradeChatRoom;
import com.sparta.petnexus.chat.repository.ChatRepository;
import com.sparta.petnexus.chat.repository.ChatRoomRedisRepository;
import com.sparta.petnexus.chat.repository.ChatRoomRepository;
import com.sparta.petnexus.chat.repository.TradeChatRepository;
import com.sparta.petnexus.chat.repository.TradeChatRoomRepository;
import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.common.redis.pubsub.RedisSubscriber;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final TradeChatRepository tradeChatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final TradeChatRoomRepository tradeChatRoomRepository;
    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    // 메세지 삭제 - DB Scheduler 적용 필요

    // 채팅방에 메시지 발송
    @Override
    @Transactional
    public void sendChatMessage(String roomId, ChatMessageDto messageDto) {
        saveMessage(roomId, messageDto);
        redisMessageListenerContainer.addMessageListener(redisSubscriber,
            chatRoomRedisRepository.getTopic(roomId));

        // Websocket 에 발행된 메시지를 redis 로 발행한다(publish)
        chatRoomRedisRepository.pushMessage(roomId, messageDto);
    }

    // 오픈채팅방 채팅 목록 조회
    @Override
    @Transactional(readOnly = true)
    public ChatListResponseDto getAllChatByRoomId(String roomId) {
        List<Chat> chatList = chatRepository.findAllByChatRoomIdOrderByCreatedAtAsc(roomId);

        return ChatListResponseDto.of(chatList);
    }

    // 오픈채팅 메세지 저장
    @Override
    @Transactional
    public void saveMessage(String roomId, ChatMessageDto requestDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHATROOM));

        Chat chat = requestDto.toEntity(chatRoom);
        chatRepository.save(chat);
    }

    // 중고거래 채팅방 채팅 목록 조회
    @Override
    @Transactional(readOnly = true)
    public TradeChatListResponseDto getAllTradeChatByRoomId(Long roomId) {
        List<TradeChat> tradeChatList = tradeChatRepository.findAllByTradeChatRoomIdOrderByCreatedAtAsc(
            roomId);

        return TradeChatListResponseDto.of(tradeChatList);
    }

    // 중고거래 채팅 메세지 저장
    @Override
    @Transactional
    public void saveTradeMessage(String roomId, ChatMessageDto requestDto) {
        TradeChatRoom tradeChatRoom = tradeChatRoomRepository.findById(Long.valueOf(roomId))
            .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_CHATROOM));

        TradeChat tradeChat = requestDto.toEntity(tradeChatRoom);
        tradeChatRepository.save(tradeChat);
    }
}