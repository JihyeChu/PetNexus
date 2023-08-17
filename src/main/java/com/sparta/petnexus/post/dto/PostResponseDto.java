package com.sparta.petnexus.post.dto;

import com.sparta.petnexus.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;

    public static PostResponseDto of(Post post){
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

}
