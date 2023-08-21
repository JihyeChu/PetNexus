package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRequestDto;
import com.sparta.petnexus.chat.dto.ChatResponseDto;

public interface ChatService {

    /**
     * 방 메세지 목록 조회
     *
     * @param roomId 조회할 채팅 방 ID
     * @return 조회된 메세지 목록
     */
    ChatListResponseDto getAllChatByRoomId(Long roomId);

    /**
     * 메세지 저장
     *
     * @param chatMessage 메세지 저장 요청정보
     * @return 메세지 저장 결과
     */
    ChatResponseDto saveMessage(ChatRequestDto chatMessage);
}