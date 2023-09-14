package com.sparta.petnexus.chat.repository;

import com.sparta.petnexus.chat.entity.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAllByOrderByCreatedAtAsc();

    List<ChatRoom> findAllByUserId(Long id);
}