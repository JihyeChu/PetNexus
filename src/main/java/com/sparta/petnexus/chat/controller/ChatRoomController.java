package com.sparta.petnexus.chat.controller;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRoomListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRoomRequestDto;
import com.sparta.petnexus.chat.dto.TradeChatRoomListResponseDto;
import com.sparta.petnexus.chat.service.ChatRoomService;
import com.sparta.petnexus.chat.service.ChatService;
import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
@Tag(name = "채팅방 관련 API", description = "채팅방 관련 API 입니다.")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @GetMapping("/openchat")
    @Operation(summary = "오픈채팅방 목록 조회")
    public ResponseEntity<ChatRoomListResponseDto> getOpenChatRooms() {
        ChatRoomListResponseDto result = chatRoomService.getOpenChatRooms();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/openchat")
    @Operation(summary = "오픈채팅방 생성")
    public ResponseEntity<ApiResponse> createOpenChatRoom(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody ChatRoomRequestDto requestDto) {

        chatRoomService.createOpenChatRoom(requestDto,
            userDetails.getUser());

        return ResponseEntity.ok().body(new ApiResponse("오픈채팅방 생성 성공", HttpStatus.CREATED.value()));
    }

    @PutMapping("/openchat/{id}")
    @Operation(summary = "오픈채팅방 수정", description = "@PathVariable 을 통해 오픈채팅방 id를 받아와, 해당 오픈채팅방의 제목 및 설명을 수정합니다.")
    public ResponseEntity<ApiResponse> updateContent(
        @Parameter(name = "roomId", description = "특정 채팅방 id", in = ParameterIn.PATH) @PathVariable Long id,
        @RequestBody ChatRoomRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        chatRoomService.updateOpenChatRoom(id, requestDto,
            userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("오픈채팅방 수정 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/tradechat")
    @Operation(summary = "중고거래 채팅방 목록 조회")
    public ResponseEntity<TradeChatRoomListResponseDto> getTradeChatRooms(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TradeChatRoomListResponseDto result = chatRoomService.getTradeChatRooms(
            userDetails.getUser());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/tradechat")
    @Operation(summary = "중고거래 채팅방 생성")
    public ResponseEntity<ApiResponse> createTradeChatRoom(
        @RequestBody ChatRoomRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        chatRoomService.createTradeChatRoom(requestDto,
            userDetails.getUser());

        return ResponseEntity.ok()
            .body(new ApiResponse("중고거래 채팅방 생성 성공", HttpStatus.CREATED.value()));
    }

    // 공통
    @GetMapping("/chat/{id}")
    @Operation(summary = "채팅방 내 채팅 목록 조회", description = "@PathVariable 을 통해 채팅방 id를 받아와, 해당 채팅방에 존재하는 채팅 목록을 조회합니다.")
    public ResponseEntity<ChatListResponseDto> getAllChatByRoomId(
        @Parameter(name = "roomId", description = "특정 채팅방 id", in = ParameterIn.PATH) @PathVariable Long id) {
        ChatListResponseDto result = chatService.getAllChatByRoomId(id);

        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/chat/{id}")
    @Operation(summary = "채팅방 삭제", description = "@PathVariable 을 통해 채팅방 Id를 받아와, 해당 채팅방을 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteChatRoom(
        @Parameter(name = "id", description = "특정 채팅방 id", in = ParameterIn.PATH) @PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        chatRoomService.deleteChatRoom(id, userDetails.getUser());

        return ResponseEntity.ok()
            .body(new ApiResponse("채팅방 삭제 성공", HttpStatus.OK.value()));

    }
}