package com.sparta.petnexus.post.dto;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.post.postComment.dto.PostCommentResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "post 조회 응답 DTO")
public class PostResponseDto {

    @Schema(description = "post id", example = "1")
    private Long id;

    @Schema(description = "post 제목")
    private String title;

    @Schema(description = "post 내용")
    private String content;

    @Schema(description = "작성자명")
    private String username;

    @Schema(description = "post에 받은 좋아요 갯수")
    private Integer like;

    @Schema(description = "post에 달린 댓글 리스트")
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
