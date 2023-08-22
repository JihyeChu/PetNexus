package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatRoomListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRoomRequestDto;
import com.sparta.petnexus.chat.dto.ChatRoomResponseDto;
import com.sparta.petnexus.user.entity.User;

public interface ChatRoomService {

    /**
     * 전체 채팅방 목록 조회
     *
     * @return 조회된 채팅방 목록
     */
    ChatRoomListResponseDto getChatRooms();

    /**
     * 채팅방 생성(제목만)
     *
     * @param requestDto 채팅방 저장 요청정보
     * @return 채팅방 저장 결과
     */
   ChatRoomResponseDto createChatRoom(ChatRoomRequestDto requestDto, User user);

    /**
     * 채팅방 제목 수정
     *
     * @param requestDto 채팅방 수정 요청정보
     * @return 채팅방 수정 결과
     */
    ChatRoomResponseDto updateChatRoom(Long id, ChatRoomRequestDto requestDto, User user);

    /**
     * 채팅방 삭제
     *
     * @param id 삭제할 채팅방 id
     */
    // 채팅방 삭제
    void deleteChatRoom(Long id, User user);
}