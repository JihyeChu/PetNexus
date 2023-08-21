package com.sparta.petnexus.post.postBookmark.entity;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="post_bookmark")
public class PostBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    public PostBookmark(Post post, User user){
        this.post = post;
        this.user = user;
    }
}
