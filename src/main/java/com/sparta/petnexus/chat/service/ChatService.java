package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatMessageDto;
import com.sparta.petnexus.chat.dto.TradeChatListResponseDto;

public interface ChatService {

    /**
     * 오픈채팅방 내 채팅 목록 조회
     *
     * @param roomId 조회할 오픈채팅 방 ID
     * @return 조회된 메세지 목록
     */
    ChatListResponseDto getAllChatByRoomId(Long roomId);

    /**
     * 오픈채팅 메세지 저장
     *
     * @param roomId     저장할 오픈채팅 방 ID
     * @param requestDto 메세지 저장 요청정보
     */
    void saveMessage(Long roomId, ChatMessageDto requestDto);

    /**
     * 중고거래 채팅방 내 채팅 목록 조회
     *
     * @param id 조회할 채팅 방 ID
     * @return 조회된 메세지 목록
     */
    TradeChatListResponseDto getAllTradeChatByRoomId(Long id);

    /**
     * 중고거래 채팅 메세지 저장
     *
     * @param roomId     저장할 중고거래 채팅 방 ID
     * @param requestDto 메세지 저장 요청정보
     */
    void saveTradeMessage(Long roomId, ChatMessageDto requestDto);
}