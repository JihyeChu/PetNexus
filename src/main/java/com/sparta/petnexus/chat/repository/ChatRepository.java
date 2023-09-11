package com.sparta.petnexus.chat.repository;

import com.sparta.petnexus.chat.entity.Chat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByChatRoomIdOrderByCreatedAtAsc(String roomId); // 방 메세지 생성날짜 기준 오름차순 정렬

    List<Chat> findTop100ByChatRoomIdOrderByCreatedAtAsc(String roomId);
}