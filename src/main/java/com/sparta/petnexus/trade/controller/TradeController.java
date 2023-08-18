package com.sparta.petnexus.trade.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.trade.dto.TradeRequestDto;
import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.service.TradeService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Builder
@RequiredArgsConstructor
@RequestMapping("/api")
public class TradeController {

    private final TradeService tradeService;

    // 거래 게시글 생성
    @PostMapping("/trade")
    public ResponseEntity<ApiResponse> createTrade(@RequestBody TradeRequestDto requestDto) {
        tradeService.createTrade(requestDto);
        return ResponseEntity.ok(new ApiResponse("거래 게시글 생성 성공", HttpStatus.OK.value()));
    }

    // 거래 게시글 전체 조회
    @GetMapping("/trade")
    public ResponseEntity<List<TradeResponseDto>> getTrade() {
        List<TradeResponseDto> tradeList = tradeService.getTrade();
        return ResponseEntity.ok().body(tradeList);
    }

    // 거래 게시글 단건 조회
    @GetMapping("/trade/{tradeId}")
    public ResponseEntity<TradeResponseDto> selectTrade(@PathVariable Long tradeId) {
        return ResponseEntity.ok().body(tradeService.selectTrade(tradeId));
    }

    // 거래 게시글 수정
    @PutMapping("/trade/{tradeId}")
    public ResponseEntity<ApiResponse> updateTrade(@RequestBody TradeRequestDto requestDto, @PathVariable Long tradeId) {
        tradeService.updateTrade(requestDto, tradeId);
        return ResponseEntity.ok().body(new ApiResponse("거래 게시글 수정 성공", HttpStatus.OK.value()));
    }

    // 거래 게시글 삭제
    @DeleteMapping("/trade/{tradeId}")
    public ResponseEntity<ApiResponse> deleteTrade(@PathVariable Long tradeId) {
        tradeService.deleteTrade(tradeId);
        return ResponseEntity.ok().body(new ApiResponse("거래 게시글 삭제 성공", HttpStatus.OK.value()));
    }

}

