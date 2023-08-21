package com.sparta.petnexus.post.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.post.dto.PostResponseDto;
import com.sparta.petnexus.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<ApiResponse> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.createPost(postRequestDto,userDetails.getUser());
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
    public ResponseEntity<ApiResponse> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.updatePost(postId, postRequestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("post 수정 성공!", HttpStatus.OK.value()));
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("post 삭제 완료!", HttpStatus.OK.value()));
    }

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<ApiResponse> createPostLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.createPostLike(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("해당 post에 좋아요를 눌렀습니다", HttpStatus.CREATED.value()));
    }

    @DeleteMapping("/post/{postId}/like")
    public ResponseEntity<ApiResponse> deletePostLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.deletePostLike(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("좋아요를 취소 하였습니다", HttpStatus.OK.value()));
    }

    @PostMapping("/post/{postId}/bookmark")
    public ResponseEntity<ApiResponse> createPostBookmark(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.createPostBookmark(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("해당 post를 북마크에 추가하였습니다.", HttpStatus.CREATED.value()));
    }

    @DeleteMapping("/post/{postId}/bookmark")
    public ResponseEntity<ApiResponse> deletePostBookmark(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.deletePostBookmark(postId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("해당 post를 북마크에 삭제하였습니다.", HttpStatus.OK.value()));
    }
}
