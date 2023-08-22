package com.sparta.petnexus.chat.controller;

import com.sparta.petnexus.chat.dto.ChatListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRoomListResponseDto;
import com.sparta.petnexus.chat.dto.ChatRoomRequestDto;
import com.sparta.petnexus.chat.dto.ChatRoomResponseDto;
import com.sparta.petnexus.chat.service.ChatRoomService;
import com.sparta.petnexus.chat.service.ChatService;
import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.RejectedExecutionException;
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

    @GetMapping("/room")
    @Operation(summary = "채팅방 목록 조회")
    public ResponseEntity<ChatRoomListResponseDto> getChatRooms() {
        ChatRoomListResponseDto result = chatRoomService.getChatRooms();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/room")
    @Operation(summary = "채팅방 생성(제목만)")
    public ResponseEntity<ChatRoomResponseDto> createChatRoom(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody ChatRoomRequestDto requestDto) {

        ChatRoomResponseDto result = chatRoomService.createChatRoom(requestDto,
            userDetails.getUser());

        return ResponseEntity.status(201).body(result);
    }

    @GetMapping("/room/{id}")
    @Operation(summary = "채팅방 내 채팅 목록 조회", description = "@PathVariable 을 통해 채팅방 id를 받아와, 해당 채팅방에 존재하는 채팅 목록을 조회합니다.")
    public ResponseEntity<ChatListResponseDto> getAllChatByRoomId(
        @Parameter(name = "roomId", description = "특정 채팅방 id", in = ParameterIn.PATH) @PathVariable Long id) {
        ChatListResponseDto result = chatService.getAllChatByRoomId(id);
        return ResponseEntity.ok(result);
    }

    // 카드 내용 수정
    @PutMapping("/room/{id}")
    @Operation(summary = "채팅방 제목 수정", description = "@PathVariable 을 통해 채팅방 id를 받아와, 해당 채팅방의 제목을 수정합니다.")
    public ResponseEntity<ChatRoomResponseDto> updateContent(
        @Parameter(name = "roomId", description = "특정 채팅방 id", in = ParameterIn.PATH) @PathVariable Long id,
        @RequestBody ChatRoomRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ChatRoomResponseDto result = chatRoomService.updateChatRoom(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/room/{id}")
    @Operation(summary = "채팅방 삭제", description = "@PathVariable 을 통해 채팅방 Id를 받아와, 해당 채팅방을 삭제합니다.")
    public ResponseEntity<ApiResponse> deleteChatRoom(
        @Parameter(name = "id", description = "특정 채팅방 id", in = ParameterIn.PATH) @PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            chatRoomService.deleteChatRoom(id, userDetails.getUser());
            return ResponseEntity.ok()
                .body(new ApiResponse("채팅방 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}