package com.sparta.petnexus.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

// Web Security 설정

@Configuration
public class StompWebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
        message
            .nullDestMatcher().permitAll()
            .simpSubscribeDestMatchers("/stomp/**").permitAll()
            .simpDestMatchers("/pub/**").authenticated()
            .simpSubscribeDestMatchers("/sub/**").authenticated()
            .simpSubscribeDestMatchers("ws/**", "/pub/**", "/sub/**").permitAll()
            //.anyMessage().denyAll();
            .anyMessage().permitAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}