package com.sparta.petnexus.post.postComment.dto;

import com.sparta.petnexus.post.postComment.entity.PostComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "comment 조회 응답 DTO")
public class PostCommentResponseDto {

    @Schema(description = "comment id", example = "1")
    private Long id;

    @Schema(description = "comment 내용")
    private String comment;

    @Schema(description = "post 제목")
    private String postTile;

    @Schema(description = "작성자명")
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
