package com.sparta.petnexus.chat.dto;

import com.sparta.petnexus.chat.entity.Chat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "오픈채팅방 채팅 목록 조회 응답 DTO")
public class ChatResponseDto {
    @Schema(description = "채팅방 아이디", example = "1")
    private Long roomId;
    @Schema(description = "작성자")
    private String sender;
    @Schema(description = "메세지 내용")
    private String message;
    @Schema(description = "메세지 생성 일자")
    private LocalDateTime createdAt;

    public static ChatResponseDto of(Chat chat) { // 오픈채팅
        return ChatResponseDto.builder()
            .roomId(chat.getChatRoom().getId())
            .sender(chat.getSender())
            .message(chat.getMessage())
            .createdAt(chat.getCreatedAt())
            .build();
    }
}