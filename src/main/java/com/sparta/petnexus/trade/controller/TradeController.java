package com.sparta.petnexus.trade.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.trade.dto.TradeRequestDto;
import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.service.TradeService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TradeController {

    private final TradeService tradeService;

    // 거래게시글 생성
    @PostMapping("/trade")
    public ResponseEntity<ApiResponse> createTrade(@RequestBody TradeRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeService.createTrade(requestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("거래게시글 생성 성공", HttpStatus.CREATED.value()));
    }

    // 거래게시글 전체 조회
    @GetMapping("/trade")
    public ResponseEntity<List<TradeResponseDto>> getTrade() {
        List<TradeResponseDto> tradeList = tradeService.getTrade();
        return ResponseEntity.ok(tradeList);
    }

    // 거래게시글 단건 조회
    @GetMapping("/trade/{tradeId}")
    public ResponseEntity<TradeResponseDto> selectTrade(@PathVariable Long tradeId) {
        return ResponseEntity.ok().body(tradeService.selectTrade(tradeId));
    }

    // 거래게시글 수정
    @PutMapping("/trade/{tradeId}")
    public ResponseEntity<ApiResponse> updateTrade(@RequestBody TradeRequestDto requestDto, @PathVariable Long tradeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeService.updateTrade(requestDto, tradeId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("거래게시글 수정 성공", HttpStatus.OK.value()));
    }

    // 거래게시글 삭제
    @DeleteMapping("/trade/{tradeId}")
    public ResponseEntity<ApiResponse> deleteTrade(@PathVariable Long tradeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeService.deleteTrade(tradeId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("거래게시글 삭제 성공", HttpStatus.OK.value()));
    }

    // 거래게시글 좋아요 생성
    @PostMapping("/trade/{tradeId}/like")
    public ResponseEntity<ApiResponse> likeTrade(@PathVariable Long tradeId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        tradeService.likeTrade(tradeId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("거래게시글 좋아요 성공", HttpStatus.ACCEPTED.value()));
    }

    @DeleteMapping("/trade/{tradeId}/like")
    public ResponseEntity<ApiResponse> dislikeTrade(@PathVariable Long tradeId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        tradeService.dislikeTrade(tradeId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("거래게시글 좋아요 취소 성공", HttpStatus.ACCEPTED.value()));
    }
}


