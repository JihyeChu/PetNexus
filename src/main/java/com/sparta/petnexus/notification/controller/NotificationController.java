package com.sparta.petnexus.notification.controller;

import com.sparta.petnexus.common.response.ApiResponse;
import com.sparta.petnexus.notification.dto.NotificationListResponseDto;
import com.sparta.petnexus.notification.service.NotificationService;
import com.sparta.petnexus.common.security.entity.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "알람 관련 API", description = "알람 관련 API 입니다.")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/connect", produces = "text/event-stream")
    @Operation(summary = "SSE 연결")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return ResponseEntity.ok(notificationService.subscribe(userDetails.getUser(), lastEventId));
    }

    @GetMapping
    @Operation(summary = "알람 목록 조회")
    public ResponseEntity<NotificationListResponseDto> getNotifications(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(notificationService.getNotifications(userDetails.getUser()));
    }

    @PatchMapping(value = "/{id}")
    @Operation(summary = "알람 읽음으로 변경")
    public ResponseEntity<ApiResponse> readNotification(@PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails
        ) {
        notificationService.readNotification(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("알람 읽음으로 변경 성공", HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "알람 삭제")
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable Long id,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.deleteNotification(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponse("알람 삭제 성공", HttpStatus.OK.value()));
    }
}

