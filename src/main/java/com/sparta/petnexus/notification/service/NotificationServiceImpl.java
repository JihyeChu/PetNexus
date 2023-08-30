package com.sparta.petnexus.notification.service;

import com.sparta.petnexus.chat.entity.TradeChatRoom;
import com.sparta.petnexus.notification.dto.NotificationListResponseDto;
import com.sparta.petnexus.notification.dto.NotificationRequestDto;
import com.sparta.petnexus.notification.dto.NotificationResponseDto;
import com.sparta.petnexus.notification.entity.Notification;
import com.sparta.petnexus.notification.entity.NotificationType;
import com.sparta.petnexus.notification.repository.EmitterRepositoryImpl;
import com.sparta.petnexus.notification.repository.NotificationRepository;
import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.post.postComment.entity.PostComment;
import com.sparta.petnexus.post.postLike.entity.PostLike;
import com.sparta.petnexus.trade.comment.entity.TradeComment;
import com.sparta.petnexus.trade.like.entity.TradeLike;
import com.sparta.petnexus.user.entity.User;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // SSE 유효시간 : 1시간
    public static final String CLIENT_BASIC_URL = "https://localhost:8080/api";
    private final NotificationRepository notificationRepository;
    private final EmitterRepositoryImpl emitterRepository;

    @Transactional
    public SseEmitter subscribe(User user, String lastEventId) {
        String emitterId = makeTimeIncludeId(user);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        String eventId = makeTimeIncludeId(user);
        sendToClient(emitter, emitterId, eventId,
            "연결되었습니다. EventStream Created. [userId=" + user.getId() + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithUserId(
                String.valueOf(user.getId()));
            events.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getKey(),
                    entry.getValue()));
        }
        return emitter;
    }

    private String makeTimeIncludeId(User user) {  // 데이터 유실 시점 파악 위함
        return user.getId() + "_" + System.currentTimeMillis();
    }

    // 특정 SseEmitter 를 이용해 알림을 보냅니다. SseEmitter 는 최초 연결 시 생성되며,
    // 해당 SseEmitter 를 생성한 클라이언트로 알림을 발송하게 됩니다.
    public void sendToClient(SseEmitter emitter, String emitterId, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                .name("sse")
                .id(eventId)
                .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            throw new BusinessException(ErrorCode.SSE_CONNECTION_ERROR);
        }
    }

    // 알람 보내기 (notificationRepository 에 저장 후 보내기)
    @Override
    public void send(NotificationRequestDto requestDto) {
        sendNotification(requestDto, saveNotification(requestDto));
    }

    // 알람 저장
    @Transactional
    public Notification saveNotification(NotificationRequestDto requestDto) {
        Notification notification = Notification.builder()
            .receiver(requestDto.getReceiver())
            .notificationType(requestDto.getNotificationType())
            .content(requestDto.getContent())
            .url(requestDto.getUrl())
            .isRead(false)
            .build();
        notificationRepository.save(notification);
        return notification;
    }

    // 알림 보내기
    @Async
    public void sendNotification(NotificationRequestDto request, Notification notification) {
        String receiverId = String.valueOf(request.getReceiver().getId());
        String eventId = receiverId + "_" + System.currentTimeMillis();
        // 유저의 모든 SseEmitter 가져옴
        Map<String, SseEmitter> emitters = emitterRepository
            .findAllEmitterStartWithByUserId(receiverId);
        emitters.forEach(
            (key, emitter) -> {
                // 데이터 캐시 저장 (유실된 데이터 처리 위함)
                emitterRepository.saveEventCache(key, notification);
                // 데이터 전송
                sendToClient(emitter, key, eventId, NotificationResponseDto.of(notification));
            }
        );
    }

    // 게시글 좋아요 버튼 클릭 시 전송되는 알람
    @Override
    @Transactional
    public void notifyToUsersThatTheyHaveReceivedLike(PostLike postLike) {
        User receiver = postLike.getPost().getUser(); // 글쓴이
        String content =
            postLike.getUser().getUsername() + "님이 \""
                + postLike.getPost().getTitle() + "\" 게시글에 대해 좋아요를 눌렀습니다.";

        String redirectUrl = CLIENT_BASIC_URL + "/posts/" + postLike.getPost().getId();

        NotificationRequestDto requestDto = NotificationRequestDto.builder()
            .notificationType(NotificationType.LIKE)
            .content(content)
            .url(redirectUrl)
            .receiver(receiver)
            .build();

        send(requestDto);
    }

    // 게시글에 댓글을 남길 시 전송되는 알람
    @Override
    @Transactional
    public void notifyToUsersThatTheyHaveReceivedComment(PostComment postComment) {
        User receiver = postComment.getPost().getUser(); // 글쓴이
        String content =
            postComment.getUser().getUsername() + "님이 \""
                + postComment.getPost().getTitle() + "\" 게시글에 대해 댓글을 남겼습니다.";

        String redirectUrl = CLIENT_BASIC_URL + "/posts/" + postComment.getPost().getId();

        NotificationRequestDto requestDto = NotificationRequestDto.builder()
            .notificationType(NotificationType.COMMENT)
            .content(content)
            .url(redirectUrl)
            .receiver(receiver)
            .build();

        send(requestDto);
    }

    // 중고거래 게시글 좋아요 버튼 클릭 시 전송되는 알람
    @Override
    @Transactional
    public void notifyToUsersThatTheyHaveReceivedLike(TradeLike tradeLike) {
        User receiver = tradeLike.getTrade().getUser(); // 글쓴이
        String content =
            tradeLike.getUser().getUsername() + "님이 \""
                + tradeLike.getTrade().getTitle() + "\" 게시글에 대해 좋아요를 눌렀습니다.";

        String redirectUrl = CLIENT_BASIC_URL + "/posts/" + tradeLike.getTrade().getId();

        NotificationRequestDto requestDto = NotificationRequestDto.builder()
            .notificationType(NotificationType.LIKE)
            .content(content)
            .url(redirectUrl)
            .receiver(receiver)
            .build();

        send(requestDto);
    }

    // 중고거래 게시글에 댓글을 남길 시 전송되는 알람
    @Override
    @Transactional
    public void notifyToUsersThatTheyHaveReceivedComment(TradeComment tradeComment) {
        User receiver = tradeComment.getTrade().getUser(); // 글쓴이
        String content =
            tradeComment.getUser().getUsername() + "님이 \""
                + tradeComment.getTrade().getTitle() + "\" 게시글에 대해 댓글을 남겼습니다.";

        String redirectUrl = CLIENT_BASIC_URL + "/posts/" + tradeComment.getTrade().getId();

        NotificationRequestDto requestDto = NotificationRequestDto.builder()
            .notificationType(NotificationType.COMMENT)
            .content(content)
            .url(redirectUrl)
            .receiver(receiver)
            .build();

        send(requestDto);
    }

    // 중고거래 게시글에서 구매 요청 시 전송되는 알람
    @Override
    @Transactional
    public void notifyToSellersThatTheyHaveReceivedTradeChatRequest(TradeChatRoom tradeChatRoom) {
        User receiver = tradeChatRoom.getTrade().getUser(); // 글쓴이(판매자)

        String content =
            tradeChatRoom.getBuyer().getUsername() + "님이 \""
                + tradeChatRoom.getTrade().getTitle() + "\" 게시글 상품에 대해 구매 요청을 하였습니다.";

        String redirectUrl = CLIENT_BASIC_URL + "/trade/" + tradeChatRoom.getTrade().getId();
        log.info(redirectUrl);

        NotificationRequestDto requestDto = NotificationRequestDto.builder()
            .notificationType(NotificationType.PURCHASE)
            .content(content)
            .url(redirectUrl)
            .receiver(receiver)
            .build();

        send(requestDto);
    }

    // 알림 목록 조회
    @Override
    @Transactional(readOnly = true)
    public NotificationListResponseDto getNotifications(User user) {
        return NotificationListResponseDto.of(
            notificationRepository.findAllByReceiverId(user.getId()));
    }

    // 알림 읽음 처리
    @Override
    @Transactional
    public void readNotification(Long notificationId, User user) {
        Notification notification = findNotification(notificationId);
        notification.read();
        notificationRepository.save(notification);
    }

    // 알람 삭제
    @Override
    @Transactional
    public void deleteNotification(Long notificationId, User user) {
        notificationRepository.delete(findNotification(notificationId));
    }

    // 알람 존재 여부
    @Override
    public Notification findNotification(Long id) {
        return notificationRepository.findById(id).orElseThrow(
            () -> new BusinessException(ErrorCode.NOT_FOUND_NOTIFICATION)
        );
    }
}
