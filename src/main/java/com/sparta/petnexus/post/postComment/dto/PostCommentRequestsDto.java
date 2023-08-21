package com.sparta.petnexus.post.postComment.dto;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.postComment.entity.PostComment;
import com.sparta.petnexus.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentRequestsDto {
    private String comment;

    public PostComment toEntity(Post post, User user){
        return PostComment.builder()
                .comment(this.comment)
                .post(post)
                .user(user)
                .build();
    }
}
