package com.sparta.petnexus.chat.entity;

import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // 오픈채팅 개설자

    private String title; // 채팅방 이름

    private String content; // 채팅방 설명

    public void updateChatRoomTitle(String title) {
        this.title = title;
    }

    public void updateChatRoomContent(String content) {
        this.content = content;
    }
}