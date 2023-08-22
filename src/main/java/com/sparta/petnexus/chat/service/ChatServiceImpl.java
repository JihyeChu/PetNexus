package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRequestDto;
import com.sparta.petnexus.chat.dto.ChatResponseDto;
import com.sparta.petnexus.chat.entity.Chat;
import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.chat.repository.ChatRepository;
import com.sparta.petnexus.chat.repository.ChatRoomRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 메세지 삭제 - DB Scheduler 적용 필요
    // 방 메세지 목록 조회
    @Transactional(readOnly = true)
    public ChatListResponseDto getAllChatByRoomId(Long roomId) {
        List<Chat> chatList = chatRepository.findAllByChatRoomIdOrderByCreatedAtAsc(roomId);

        return ChatListResponseDto.of(chatList);
    }

    // 메세지 저장
    @Transactional
    public ChatResponseDto saveMessage(ChatRequestDto requestDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(requestDto.getRoomId())
            .orElseThrow(() -> new NoSuchElementException("해당 채팅방이 존재하지 않습니다 id : " + requestDto.getRoomId()));

        Chat chat = requestDto.toEntity(chatRoom);
        chatRepository.save(chat);

        return ChatResponseDto.of(chat);
    }
}