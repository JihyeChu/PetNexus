package com.sparta.petnexus.post.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto) {
        return postService.createPost(postRequestDto);
    }

    @GetMapping("post")
    public List<PostResponseDto> getPosts(){
        return postService.getPosts();
    }

    @GetMapping("post/{postId}")
    public PostResponseDto getPostId(@PathVariable Long postId) {
        return postService.getPostId(postId);
    }

    @PutMapping("/post/{postId}")
    public PostResponseDto updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        return postService.updatePost(postId, postRequestDto);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }

}
