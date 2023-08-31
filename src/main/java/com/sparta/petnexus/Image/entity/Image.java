package com.sparta.petnexus.Image.entity;

import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.trade.entity.Trade;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Table(name="Images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="chatRoom_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="trade_id")
    private Trade trade;

    public Image(Post post,String urlText){
        this.post = post;
        this.imageUrl = urlText;
    }

    public Image(Trade trade,String urlText){
        this.trade = trade;
        this.imageUrl = urlText;
    }

    public Image(ChatRoom chatRoom,String urlText){
        this.chatRoom = chatRoom;
        this.imageUrl = urlText;
    }
}
