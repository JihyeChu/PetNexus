package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatMessageDto;
import com.sparta.petnexus.chat.dto.TradeChatListResponseDto;

public interface ChatService {

    /**
     * 채팅방에 메시지 발송
     *
     * @param roomId 발송할 오픈채팅 방 ID
     */
    void sendChatMessage(String roomId, ChatMessageDto requestDto);
    /**
     * 오픈채팅방 내 채팅 목록 조회
     *
     * @param roomId 조회할 오픈채팅 방 ID
     * @return 조회된 메세지 목록
     */
    ChatListResponseDto getAllChatByRoomId(String roomId);

    /**
     * 오픈채팅 메세지 저장
     *
     * @param roomId     저장할 오픈채팅 방 ID
     * @param requestDto 메세지 저장 요청정보
     */
    void saveMessage(String roomId, ChatMessageDto requestDto);

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
    void saveTradeMessage(String roomId, ChatMessageDto requestDto);
}