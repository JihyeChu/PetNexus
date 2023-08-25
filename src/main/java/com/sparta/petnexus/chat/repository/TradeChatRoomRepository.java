package com.sparta.petnexus.chat.repository;

import com.sparta.petnexus.chat.entity.TradeChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeChatRoomRepository extends JpaRepository<TradeChatRoom, Long> {

    List<TradeChatRoom> findAllByBuyer_IdOrderByCreatedAtAsc(Long UserId);
}