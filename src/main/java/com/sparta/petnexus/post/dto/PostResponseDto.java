package com.sparta.petnexus.post.dto;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.postComment.dto.PostCommentResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private Integer like;
    private List<PostCommentResponseDto> postComments;

    public static PostResponseDto of(Post post){
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .username(post.getUser().getUsername())
                .like(post.getPostLikes().size())
                .postComments(post.getPostComments().stream().map(PostCommentResponseDto::of).toList())
                .build();
    }
}
