package com.sparta.petnexus.Image.entity;

import com.sparta.petnexus.chat.entity.ChatRoom;
import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.trade.entity.Trade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="chatRoom_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name="trade_id")
    private Trade trade;

    public Image(Post post,String urlText){
        this.post = post;
        this.imageUrl = urlText;
    }
}
