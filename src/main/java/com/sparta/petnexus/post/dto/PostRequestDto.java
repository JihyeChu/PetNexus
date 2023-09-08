package com.sparta.petnexus.post.dto;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "post 생성/수정 요청 DTO")
public class PostRequestDto {

    @Schema(description = "post 제목")
    private String title;

    @Schema(description = "post 내용")
    private String content;

    public Post toEntity(User user){
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }
}
