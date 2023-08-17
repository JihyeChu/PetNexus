package com.sparta.petnexus.post.dto;

import com.sparta.petnexus.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRequestDto {
    private String title;
    private String content;

    //  new Post 같은 것
    public Post toEntity(){
        Post post = Post.builder()
                .title(this.title)
                .content(this.content)
                .build();
        return post;
    }
}
