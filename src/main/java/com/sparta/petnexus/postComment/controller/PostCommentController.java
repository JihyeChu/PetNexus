package com.sparta.petnexus.postComment.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.postComment.dto.PostCommentRequestsDto;
import com.sparta.petnexus.postComment.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<ApiResponse> createPostComment(@PathVariable Long postId,
                                                         @RequestBody PostCommentRequestsDto postCommentRequestsDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        postCommentService.createPostComment(postId,postCommentRequestsDto,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("댓글 달기 완료!", HttpStatus.CREATED.value()));
    }

    @PutMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<ApiResponse> updatePostComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                         @RequestBody PostCommentRequestsDto postCommentRequestsDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        postCommentService.updatePostComment(postId,commentId,postCommentRequestsDto,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("댓글 수정 완료!", HttpStatus.OK.value()));
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<ApiResponse> deletePostComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        postCommentService.deletePostComment(postId,commentId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("댓글 삭제 완료!", HttpStatus.OK.value()));
    }


}
