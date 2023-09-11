package com.sparta.petnexus.chat.entity;

import com.sparta.petnexus.Image.entity.Image;
import com.sparta.petnexus.chat.dto.ChatRoomRequestDto;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
@Table(name="chat_room")
public class ChatRoom extends Timestamped implements Serializable {

    // Redis 에 저장되는 객체들은 Serialize(직렬화) 가능해야 하므로
    // Serializable 을 참조하도록 선언하고 serialVersionUID 를 세팅한다.
    @Serial
    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 오픈채팅 개설자

    private String title; // 채팅방 이름

    private String content; // 채팅방 설명

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true)
    private List<Chat> ChatMessages = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", orphanRemoval = true)
    private List<Image> image = new ArrayList<>();

    public void update(ChatRoomRequestDto requestDto){
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}