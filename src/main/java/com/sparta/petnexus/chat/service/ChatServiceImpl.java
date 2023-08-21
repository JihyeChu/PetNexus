package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRequestDto;
import com.sparta.petnexus.chat.dto.ChatResponseDto;
import com.sparta.petnexus.chat.entity.Chat;
import com.sparta.petnexus.chat.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    // 메세지 삭제 - DB Scheduler 적용 필요
    // 방 메세지 목록 조회
    @Transactional(readOnly = true)
    public ChatListResponseDto getAllChatByRoomId(Long roomId) {
        List<ChatResponseDto> chatResponseDtos = chatRepository.findAllByRoomIdOrderByCreatedAtAsc(
            roomId).stream().map(ChatResponseDto::of).toList();
        return new ChatListResponseDto(chatResponseDtos);
    }

    // 메세지 저장
    @Transactional
    public ChatResponseDto saveMessage(ChatRequestDto chatMessage) {
        Chat chat = chatMessage.toEntity();
        chatRepository.save(chat);
        return ChatResponseDto.of(chat);
    }
}