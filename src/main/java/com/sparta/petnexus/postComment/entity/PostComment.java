package com.sparta.petnexus.postComment.entity;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.postComment.dto.PostCommentRequestsDto;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="post_comment")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    public void update(PostCommentRequestsDto postCommentRequestsDto){
        this.comment = postCommentRequestsDto.getComment();
    }

}
