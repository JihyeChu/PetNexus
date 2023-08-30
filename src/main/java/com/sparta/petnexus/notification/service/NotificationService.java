package com.sparta.petnexus.notification.service;

import com.sparta.petnexus.chat.entity.TradeChatRoom;
import com.sparta.petnexus.notification.dto.NotificationListResponseDto;
import com.sparta.petnexus.notification.dto.NotificationRequestDto;
import com.sparta.petnexus.notification.entity.Notification;
import com.sparta.petnexus.post.postComment.entity.PostComment;
import com.sparta.petnexus.post.postLike.entity.PostLike;
import com.sparta.petnexus.trade.comment.entity.TradeComment;
import com.sparta.petnexus.trade.like.entity.TradeLike;
import com.sparta.petnexus.user.entity.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    // 게시글에 좋아요 버튼 클릭 시
    void notifyToUsersThatTheyHaveReceivedLike(PostLike postLike);
    // 게시글에 댓글 작성 시
    void notifyToUsersThatTheyHaveReceivedComment(PostComment postComment);
    // 중고거래 게시글에 좋아요 버튼 클릭 시
    void notifyToUsersThatTheyHaveReceivedLike(TradeLike tradeLike);
    // 중고거래 게시글에 댓글 작성 시
    void notifyToUsersThatTheyHaveReceivedComment(TradeComment tradeComment);
    // 중고상품 구매 요청 시
    void notifyToSellersThatTheyHaveReceivedTradeChatRequest(TradeChatRoom tradeChatRoom);
    SseEmitter subscribe(User user, String lastEventId);
    void send(NotificationRequestDto request);
    void sendToClient(SseEmitter emitter, String emitterId, String eventId, Object data);
    NotificationListResponseDto getNotifications(User user);
    void readNotification(Long notificationId, User user);
    void deleteNotification(Long notificationId, User user);
    Notification findNotification(Long notificationId);
}