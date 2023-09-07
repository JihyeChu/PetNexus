package com.sparta.petnexus.trade.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import com.sparta.petnexus.post.dto.PostRequestDto;
import com.sparta.petnexus.trade.dto.TradeRequestDto;
import com.sparta.petnexus.trade.dto.TradeResponseDto;
import com.sparta.petnexus.trade.service.TradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name="trade", description = "거래게시글/좋아요/북마크 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/trade")
    @Operation(summary = "거래게시글 생성", description = "@RequestBody를 통해 게시글 정보를 넘겨주고 생성합니다.")
    public ResponseEntity<ApiResponse> createTrade(@ModelAttribute TradeRequestDto requestDto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @RequestPart(value = "imageFiles", required = false) List<MultipartFile> files) throws IOException {
        tradeService.createTrade(requestDto, userDetails.getUser(),files);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("거래게시글 생성 성공", HttpStatus.CREATED.value()));
    }

    @GetMapping("/trade")
    @Operation(summary = "거래게시글 전체 조회", description = "거래게시글을 조회합니다.")
    public ResponseEntity<Page<TradeResponseDto>> getTrade(@RequestParam("page") int page,
                                                           @RequestParam("size") int size,
                                                           @RequestParam("sortBy") String sortBy,
                                                           @RequestParam("isAsc") boolean isAsc){
        Page<TradeResponseDto> tradeList = tradeService.getTrade(page-1, size, sortBy, isAsc);
        return ResponseEntity.ok().body(tradeList);
    }
    @GetMapping("/trade/search")
    @Operation(summary = "trade 검색", description = "@RequestParam으로 keyword를 입력받아 해당 trade를 조회합니다.")
    public ResponseEntity<Page<TradeResponseDto>> searchTrade(@RequestParam("keyword") String keyword,   @RequestParam("page") int page,
                                                              @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page -1, size);
        Page<TradeResponseDto> searchList = tradeService.searchTrade(keyword, pageable);
        return ResponseEntity.ok().body(searchList);
    }

    @GetMapping("/trade/{tradeId}")
    @Operation(summary = "거래게시글 단건 조회", description = "@PathVariable를 통해 조회하고자 하는 게시글을 받아옵니다.")
    public ResponseEntity<TradeResponseDto> selectTrade(
        @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId) {
        return ResponseEntity.ok().body(tradeService.selectTrade(tradeId));
    }

    // 거래게시글 수정
    @PutMapping("/trade/{tradeId}")
    @Operation(summary = "거래게시글 수정", description = "@PathVariable를 통해 수정하고자 하는 게시글, 수정하고자 하는 정보 requestDto를 받아 수정합니다. ")
    public ResponseEntity<ApiResponse> updateTrade(
            @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId, @RequestBody TradeRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeService.updateTrade(requestDto, tradeId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("거래게시글 수정 성공", HttpStatus.OK.value()));
    }

    // 거래게시글 삭제
    @DeleteMapping("/trade/{tradeId}")
    @Operation(summary = "거래게시글 삭제", description = "@PathVariable를 통해 삭제하고자 하는 게시글을 받아 삭제합니다. ")
    public ResponseEntity<ApiResponse> deleteTrade(
            @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeService.deleteTrade(tradeId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("거래게시글 삭제 성공", HttpStatus.OK.value()));
    }

    // 거래게시글 좋아요
    @PostMapping("/trade/{tradeId}/like")
    @Operation(summary = "거래게시글 좋아요", description = "@PathVariable를 통해 게시글을 받아와 좋아요를 생성합니다.")
    public ResponseEntity<ApiResponse> likeTrade(
            @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeService.likeTrade(tradeId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("거래게시글 좋아요 성공", HttpStatus.ACCEPTED.value()));
    }

    // 거래게시글 좋아요 취소
    @DeleteMapping("/trade/{tradeId}/like")
    @Operation(summary = "거래게시글 좋아요 취소", description = "@PathVariable를 통해 게시글을 받아와 좋아요 취소합니다.")
    public ResponseEntity<ApiResponse> dislikeTrade(
            @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeService.dislikeTrade(tradeId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("거래게시글 좋아요 취소 성공", HttpStatus.ACCEPTED.value()));
    }

    // 거래게시글 북마크 생성
    @PostMapping("/trade/{tradeId}/bookmark")
    @Operation(summary = "거래게시글 북마크", description = "@PathVariable를 통해 게시글을 받아와 북마크를 생성합니다.")
    public ResponseEntity<ApiResponse> doBookmark(
            @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeService.doBookmark(tradeId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("거래게시글 북마크 성공",  HttpStatus.ACCEPTED.value()));
    }

    // 거래게시글 북마크 취소
    @DeleteMapping("/trade/{tradeId}/bookmark")
    @Operation(summary = "거래게시글 북마크 취소", description = "@PathVariable를 통해 게시글을 받아와 북마크 취소합니다.")
    public ResponseEntity<ApiResponse> undoBookmark(
            @Parameter(description = "해당 게시글 id", in = ParameterIn.PATH) @PathVariable Long tradeId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        tradeService.undoBookmark(tradeId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse("거래게시글 북마크 취소 성공", HttpStatus.ACCEPTED.value()));
    }

}


