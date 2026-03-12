package com.ShareText.demo.WebSocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final  RoomWebSocketHandler socketHandler;

    public WebSocketConfig(RoomWebSocketHandler socketHandler){
        this.socketHandler = socketHandler;
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("Helooo");
        registry.addHandler(socketHandler, "/ws/room").setAllowedOrigins("*");
    }
}
