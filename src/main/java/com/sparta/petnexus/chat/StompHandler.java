package com.sparta.petnexus.chat;

import com.sparta.petnexus.chat.dto.ChatMessageDto;
import com.sparta.petnexus.chat.entity.ChatType;
import com.sparta.petnexus.chat.repository.ChatRoomRedisRepository;
import com.sparta.petnexus.common.exception.BusinessException;
import com.sparta.petnexus.common.exception.ErrorCode;
import com.sparta.petnexus.common.security.jwt.TokenProvider;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final TokenProvider tokenProvider; // jwt 토큰 인증 핸들러
    private final ChatRoomRedisRepository chatRoomRedisRepository;

    @Autowired
    public StompHandler(@Lazy ChatRoomRedisRepository chatRoomRedisRepository, TokenProvider tokenProvider) {
        this.chatRoomRedisRepository = chatRoomRedisRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 헤더에 있는 토큰값을 가져오기 위해 StompHeaderAccessor.wrap()
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("###########"+accessor.getCommand());
        // websocket 을 통해 들어온 요청이 처리 되기 전 실행된다.
        // websocket 연결시 헤더의 jwt token 유효성 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String jwt = accessor.getFirstNativeHeader("Authorization");
            String token = tokenProvider.getTokenStompHeader(jwt);
            if (!tokenProvider.validToken(token)) {
                throw new BusinessException(ErrorCode.INVALID_AUTH_TOKEN);
            }
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) { // 채팅룸 구독요청

            // header 정보에서 구독 destination 정보를 얻고, roomId를 추출한다.
            String roomId = getRoomId(
                Optional.ofNullable((String) message.getHeaders().get("simpDestination"))
                    .orElse("InvalidRoomId"));

            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser"))
                .map(Principal::getName).orElse("UnknownUser");

            // 클라이언트 입장 메시지를 채팅방에 발송
            sendEnterMessage(
                ChatMessageDto.builder()
                    .type(ChatType.ENTER)
                    .roomId(roomId)
                    .sender(name)
                    .build());
            log.info("SUBSCRIBED {}, {}", name, roomId);

            // Websocket 에 발행된 메시지를 redis 로 발행한다(publish)
            chatRoomRedisRepository.enterChatRoom(roomId);
        }
        return message;
    }

    public void sendEnterMessage(ChatMessageDto messageDto) {
        if (ChatType.ENTER.equals(messageDto.getType())) {
            messageDto.setMessage(messageDto.getSender() + "님이 입장하셨습니다.");
            log.info(messageDto.getType());
        }
    }

    // destination 정보에서 roomId 추출
    public String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return "";
        }
    }
    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        switch (Objects.requireNonNull(accessor.getCommand())) {
            case CONNECT:
                // 유저가 Websocket 으로 connect()를 한 뒤 호출됨

                break;
            case DISCONNECT:
                log.info("DISCONNECT");
                log.info("sessionId: {}",sessionId);
                log.info("channel:{}",channel);
                log.info(message);
                // 유저가 Websocket 으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생함(페이지 이동~ 브라우저 닫기 등)
                break;
            default:
                break;
        }

    }
}
