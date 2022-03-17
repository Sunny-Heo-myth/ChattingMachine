package com.sunny.chattingmachine.config;

import com.sunny.chattingmachine.handler.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketHandler socketHandler;

    @Autowired
    public WebSocketConfig(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    // register socketHandler for ws://localhost:8087/chatting/{roomId}
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler, "/chatting/{roomId}");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        // text and binary message should be under 500kb
        container.setMaxTextMessageBufferSize(500000);
        container.setMaxBinaryMessageBufferSize(500000);
        return container;
    }

}
