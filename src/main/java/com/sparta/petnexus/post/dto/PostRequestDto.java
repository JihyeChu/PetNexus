package com.sparta.petnexus.post.dto;

import com.sparta.petnexus.post.entity.Post;
import com.sparta.petnexus.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRequestDto {
    private String title;
    private String content;

    public Post toEntity(User user){
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .build();
    }
}
