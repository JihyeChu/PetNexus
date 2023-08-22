package com.sparta.petnexus.trade.comment.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.trade.comment.dto.TradeCommentRequestDto;
import com.sparta.petnexus.trade.comment.service.TradeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TradeCommentController {

    private final TradeCommentService tradeCommentService;

    // 거래게시글 댓글 생성
    @PostMapping("/trade/{tradeId}/comment")
    public ResponseEntity<ApiResponse> createComment(@PathVariable Long tradeId, @RequestBody TradeCommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        tradeCommentService.createComment(tradeId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("거래게시글 댓글 생성 성공", HttpStatus.CREATED.value()));
    }

    // 거래게시글 댓글 수정
    @PutMapping("/trade/{tradeId}/comment/{commentId}")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable Long tradeId, @PathVariable Long commentId, @RequestBody TradeCommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        tradeCommentService.updateComment(tradeId, commentId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("거래게시글 댓글 수정 성공", HttpStatus.OK.value()));
    }

    // 거래게시글 댓글 삭제
    @DeleteMapping("/trade/{tradeId}/comment/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Long tradeId, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        tradeCommentService.deleteComment(tradeId, commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("거래게시글 댓글 삭제 성공", HttpStatus.OK.value()));
    }
}
