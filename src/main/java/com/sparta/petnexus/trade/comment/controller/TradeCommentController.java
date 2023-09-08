package com.sparta.petnexus.trade.comment.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.trade.comment.dto.TradeCommentRequestDto;
import com.sparta.petnexus.trade.comment.service.TradeCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "trade comment", description = "거래게시글 댓글 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TradeCommentController {

    private final TradeCommentService tradeCommentService;

    // 거래게시글 댓글 생성
    @PostMapping("/trade/{tradeId}/comment")
    @Operation(summary = "거래게시글 댓글 생성", description = "@PathVariable와 requestDto를 통해 게시글을 받아오고 댓글을 생성합니다.")
    public ResponseEntity<ApiResponse> createComment(
            @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId, @RequestBody TradeCommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeCommentService.createComment(tradeId, requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("거래게시글 댓글 생성 성공", HttpStatus.CREATED.value()));
    }

    // 거래게시글 댓글 수정
    @PutMapping("/trade/{tradeId}/comment/{commentId}")
    @Operation(summary = "거래게시글 댓글 수정", description = "@PathVariable와 requestDto를 통해 게시글을 받아오고 댓글을 수정합니다.")
    public ResponseEntity<ApiResponse> updateComment(
            @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId,
            @Parameter(description = "해당 댓글 id", in = ParameterIn.PATH) @PathVariable Long commentId, @RequestBody TradeCommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeCommentService.updateComment(tradeId, commentId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("거래게시글 댓글 수정 성공", HttpStatus.OK.value()));
    }

    // 거래게시글 댓글 삭제
    @DeleteMapping("/trade/{tradeId}/comment/{commentId}")
    @Operation(summary = "거래게시글 댓글 삭제", description = "@PathVariable를 통해 게시글을 받아오고 댓글을 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteComment(
            @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId,
            @Parameter(description = "해당 댓글 id", in = ParameterIn.PATH) @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeCommentService.deleteComment(tradeId, commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("거래게시글 댓글 삭제 성공", HttpStatus.OK.value()));
    }
}
