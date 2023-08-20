package com.sparta.petnexus.postComment.dto;

import com.sparta.petnexus.postComment.entity.PostComment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCommentResponseDto {
    private Long id;
    private String comment;
    private String postTile;
    private String username;

    public static PostCommentResponseDto of(PostComment postComment){
        return PostCommentResponseDto.builder()
                .id(postComment.getId())
                .comment(postComment.getComment())
                .postTile(postComment.getPost().getTitle())
                .username(postComment.getUser().getUsername())
                .build();
    }
}
