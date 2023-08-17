package com.sparta.petnexus.post.entity;

import com.sparta.petnexus.post.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    // user 연관 관계

    public void update(PostRequestDto postRequestDto){
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }
}
