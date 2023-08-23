package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRequestDto;
import com.sparta.petnexus.chat.dto.ChatResponseDto;

public interface ChatService {

    /**
     * 채팅방 내 채팅 목록 조회
     *
     * @param id 조회할 채팅 방 ID
     * @return 조회된 메세지 목록
     */
    ChatListResponseDto getAllChatByRoomId(Long id);

    /**
     * 메세지 저장
     *
     * @param requestDto 메세지 저장 요청정보
     * @return 메세지 저장 결과
     */
    ChatResponseDto saveMessage(ChatRequestDto requestDto);
}