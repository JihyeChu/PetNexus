package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatRoomListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRoomRequestDto;
import com.sparta.petnexus.chat.dto.TradeChatRoomListResponseDto;
import com.sparta.petnexus.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ChatRoomService {

    /**
     * 오픈채팅방 목록 조회
     *
     * @return 조회된 오픈채팅방 목록
     */
    ChatRoomListResponseDto getOpenChatRooms();

    /**
     * 오픈채팅방 생성
     *
     * @param requestDto 오픈채팅방 저장 요청정보
     * @param user       오픈채팅방 생성 요청자
     * @param files       오픈채팅방 생성 첨부 파일
     */
    void createOpenChatRoom(ChatRoomRequestDto requestDto, User user, List<MultipartFile> files) throws IOException;

    /**
     * 오픈채팅방 제목 수정
     *
     * @param id         수정할 오픈채팅방 id
     * @param requestDto 오픈채팅방 수정 요청정보
     * @param user       오픈채팅방 수정 요청자
     */
    void updateOpenChatRoom(Long id, ChatRoomRequestDto requestDto, User user);

    /**
     * 중고거래 채팅방 목록 조회
     *
     * @param user 사용자 별 목록 조회
     * @return 조회된 중고거래 채팅방 목록
     */
    TradeChatRoomListResponseDto getTradeChatRooms(User user);

    /**
     * 중고거래 채팅방 생성
     * @param id 중고거래 상품 id
     * @param user       중고거래 채팅방 생성 요청자
     */
    void createTradeChatRoom(Long id, User user);

    /**
     * 오픈채팅방 삭제
     *
     * @param id   삭제할 채팅방 id
     * @param user 오픈채팅방 삭제 요청자
     */
    // 채팅방 삭제
    void deleteChatRoom(Long id, User user);

    /**
     * 중고거래 채팅방 삭제
     *
     * @param id   삭제할 채팅방 id
     * @param user 중고거래 채팅방 삭제 요청자
     */
    // 채팅방 삭제
    void deleteTradeChatRoom(Long id, User user);
}