package com.sparta.petnexus.chat.repository;

import com.sparta.petnexus.chat.entity.TradeChat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeChatRepository extends JpaRepository<TradeChat, Long> {

    List<TradeChat> findAllByTradeChatRoomIdOrderByCreatedAtAsc(Long roomId); // 방 메세지 생성날짜 기준 오름차순 정렬
}