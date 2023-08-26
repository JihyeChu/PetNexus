package com.sparta.petnexus.chat.service;

import com.sparta.petnexus.chat.dto.ChatRoomListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRoomRequestDto;
import com.sparta.petnexus.chat.dto.TradeChatRoomListResponseDto;
import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.chat.entity.TradeChatRoom;
import com.sparta.petnexus.chat.repository.ChatRoomRepository;
import com.sparta.petnexus.chat.repository.TradeChatRoomRepository;
import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.trade.entity.Trade;
import com.sparta.petnexus.trade.repository.TradeRepository;
import com.sparta.petnexus.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final TradeChatRoomRepository tradeChatRoomRepository;
    private final TradeRepository tradeRepository;

    // 오픈채팅방 목록 조회
    @Override
    @Transactional(readOnly = true)
    public ChatRoomListResponseDto getOpenChatRooms() {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllByOrderByCreatedAtAsc();

        return ChatRoomListResponseDto.of(chatRoomList);
    }

    // 오픈채팅방 생성
    @Override
    public void createOpenChatRoom(ChatRoomRequestDto requestDto, User user) {
        ChatRoom chatRoom = requestDto.toEntity(user);

        chatRoomRepository.save(chatRoom);
    }

    // 오픈채팅방 수정
    @Override
    @Transactional
    public void updateOpenChatRoom(Long id, ChatRoomRequestDto requestDto,
        User user) {
        ChatRoom chatRoom = findChatRoom(id);

        if (!chatRoom.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ONLY_MASTER_EDIT);
        }
        chatRoom.updateChatRoomTitle(requestDto.getTitle());
        chatRoom.updateChatRoomContent(requestDto.getContent());
    }

    // 중고거래 채팅방 목록 조회
    @Override
    @Transactional(readOnly = true)
    public TradeChatRoomListResponseDto getTradeChatRooms(User user) {
        List<TradeChatRoom> chatRoomList = tradeChatRoomRepository.findAllByBuyer_IdOrderByCreatedAtAsc(
            user.getId());

        return TradeChatRoomListResponseDto.of(chatRoomList);
    }

    // 중고거래 채팅방 생성
    @Override
    public void createTradeChatRoom(Long tradeId, User user) {
        Trade trade = findTrade(tradeId);

        TradeChatRoom tradeChatRoom = TradeChatRoom.builder()
            .sellerId(trade.getUser().getId())
            .buyer(user)
            .trade(trade).build();

        tradeChatRoomRepository.save(tradeChatRoom);
    }

    // 오픈채팅방 삭제
    @Override
    @Transactional
    public void deleteChatRoom(Long id, User user) {
        ChatRoom chatRoom = findChatRoom(id);

        if (!chatRoom.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.ONLY_MASTER_DELETE);
        }
        chatRoomRepository.delete(chatRoom);
    }

    // 중고거래 채팅방 삭제
    @Override
    @Transactional
    public void deleteTradeChatRoom(Long id, User user) {
        TradeChatRoom tradeChatRoom = findTradeChatRoom(id);

        if (!tradeChatRoom.getSellerId().equals(user.getId()) || !tradeChatRoom.getBuyer().getId()
            .equals(user.getId())) {
            throw new BusinessException(ErrorCode.ONLY_SELLER_OR_BUYER_DELETE);
        }
        tradeChatRoomRepository.delete(tradeChatRoom);
    }

    private ChatRoom findChatRoom(long id) {
        return chatRoomRepository.findById(id).orElseThrow(() ->
            new BusinessException(ErrorCode.NOT_FOUND_CHATROOM));
    }

    private TradeChatRoom findTradeChatRoom(long id) {
        return tradeChatRoomRepository.findById(id).orElseThrow(() ->
            new BusinessException(ErrorCode.NOT_FOUND_CHATROOM));
    }

    public Trade findTrade(Long tradeId) {
        return tradeRepository.findById(tradeId).orElseThrow(
            () -> new BusinessException(ErrorCode.NOT_FOUND_TRADE)
        );
    }
}