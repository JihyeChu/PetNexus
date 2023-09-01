package com.sparta.petnexus.chat.config;

import com.sparta.petnexus.chat.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@EnableWebSocketMessageBroker
@Configuration
@RequiredArgsConstructor
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler; // jwt 토큰 인증 핸들러

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat") // 여기로 웹소켓 생성
            .addInterceptors()
           // .setAllowedOrigins("http://localhost:8080")
            .setAllowedOriginPatterns("*");
            // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket 이 동작할 수 있게 설정
            // api 통신 시, withSockJS() 설정을 빼야됨
            // 웹소켓을 지원하지 않는 브라우저에 폴백 옵션을 활성화하는데 사용됩니다.
            //.withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 발행하는 요청 url -> 메시지를 보낼 때
        registry.setApplicationDestinationPrefixes("/pub");
        // 메시지를 구독하는 요청 url -> 메시지를 받을 때
        registry.enableSimpleBroker("/sub");
    }

    @Override
    // configureClientInboundChannel: jwt 토큰 검증을 위해 생성한 stompHandler 를 인터셉터로 지정해준다.
    public void configureClientInboundChannel(ChannelRegistration registration) { // 핸들러 등록
        // connect / disconnect 인터셉터
        registration.interceptors(stompHandler);
        // 엔드포인트 경로를 .permitAll 해주면 해결된다.
    }
}