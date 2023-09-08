package com.sparta.petnexus.post.postComment.entity;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.postComment.dto.PostCommentRequestsDto;
import com.sparta.petnexus.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="post_comment")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    public void update(PostCommentRequestsDto postCommentRequestsDto){
        this.comment = postCommentRequestsDto.getComment();
    }

}
