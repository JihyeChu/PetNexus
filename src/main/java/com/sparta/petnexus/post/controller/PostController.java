package com.sparta.petnexus.post.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<ApiResponse> createPost(@RequestBody PostRequestDto postRequestDto) {
        postService.createPost(postRequestDto);
        return ResponseEntity.ok().body(new ApiResponse("post 생성 성공!", HttpStatus.CREATED.value()));
    }

    @GetMapping("/post")
    public ResponseEntity<List<PostResponseDto>> getPosts(){
        return ResponseEntity.ok(postService.getPosts());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> getPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostId(postId));
    }

    @PutMapping("/post/{postId}")
    public ResponseEntity<ApiResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        postService.updatePost(postId, postRequestDto);
        return ResponseEntity.ok().body(new ApiResponse("post 수정 성공!", HttpStatus.OK.value()));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().body(new ApiResponse("post 삭제 완료!", HttpStatus.OK.value()));
    }

}
