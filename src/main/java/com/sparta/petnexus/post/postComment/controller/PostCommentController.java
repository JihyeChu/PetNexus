package com.sparta.petnexus.post.postComment.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.post.postComment.dto.PostCommentRequestsDto;
import com.sparta.petnexus.post.postComment.service.PostCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "post comment 관련 API", description = "post comment CUD")
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/post/{postId}/comment")
    @Operation(summary = "post comment 생성", description = "@PathVariable로 postId와 requestDto를 받아 해당하는 post에 댓글을 만듭니다.")
    public ResponseEntity<ApiResponse> createPostComment(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId,
                                                         @RequestBody PostCommentRequestsDto postCommentRequestsDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        postCommentService.createPostComment(postId,postCommentRequestsDto,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("댓글 달기 완료!", HttpStatus.CREATED.value()));
    }

    @PutMapping("/post/{postId}/comment/{commentId}")
    @Operation(summary = "post comment 수정", description = "@PathVariable로 postId,commentId와 requestDto를 받아 commentId에 해당하는 댓글을 수정합니다. ")
    public ResponseEntity<ApiResponse> updatePostComment(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId,
            @Parameter(name="commentId",description = "특정 comment id",in= ParameterIn.PATH) @PathVariable Long commentId,
                                                         @RequestBody PostCommentRequestsDto postCommentRequestsDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        postCommentService.updatePostComment(postId,commentId,postCommentRequestsDto,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("댓글 수정 완료!", HttpStatus.OK.value()));
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    @Operation(summary = "post comment 삭제", description = "@PathVariable로 postId,commentId와 requestDto를 받아 commentId에 해당하는 댓글을 삭제합니다. ")
    public ResponseEntity<ApiResponse> deletePostComment(
            @Parameter(name="postId",description = "특정 post id",in= ParameterIn.PATH) @PathVariable Long postId,
            @Parameter(name="commentId",description = "특정 comment id",in= ParameterIn.PATH) @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        postCommentService.deletePostComment(postId,commentId,userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("댓글 삭제 완료!", HttpStatus.OK.value()));
    }


}
