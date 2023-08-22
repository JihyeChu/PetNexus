package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatRoomListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRoomRequestDto;
import com.sparta.petnexus.chat.dto.ChatRoomResponseDto;
import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.chat.repository.ChatRoomRepository;
import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService{

    private final ChatRoomRepository chatRoomRepository;

    // 전체 채팅방 목록 조회
    public ChatRoomListResponseDto getChatRooms() {
        List<ChatRoom> cardList = chatRoomRepository.findAllByOrderByCreatedAtAsc();

        return ChatRoomListResponseDto.of(cardList);

    }

    // 채팅방 생성(제목만)
    public ChatRoomResponseDto createChatRoom(ChatRoomRequestDto requestDto, User user) {
        ChatRoom chatRoom = requestDto.toEntity(user);

        chatRoomRepository.save(chatRoom);

        return ChatRoomResponseDto.of(chatRoom);
    }

    // 채팅방 제목 수정
    @Transactional
    public ChatRoomResponseDto updateChatRoom(Long id, ChatRoomRequestDto requestDto, User user) {
        ChatRoom chatRoom = findChatRoom(id);

        if (!chatRoom.getMasterId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ONLY_MASTER_EDIT);
        }
        chatRoom.updateChatRoom(requestDto.getName());

        return ChatRoomResponseDto.of(chatRoom);
    }

    // 채팅방 삭제
    public void deleteChatRoom(Long id, User user) {
        ChatRoom chatRoom = findChatRoom(id);

        if (!chatRoom.getMasterId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ONLY_MASTER_DELETE);
        }
        chatRoomRepository.delete(chatRoom);
    }

    private ChatRoom findChatRoom(long id) {
        return chatRoomRepository.findById(id).orElseThrow(() ->
            new BusinessException(ErrorCode.NOT_FOUND_CHATROOM));
    }
}