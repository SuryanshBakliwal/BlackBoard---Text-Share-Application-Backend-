package com.ShareText.demo.WebSocket;


import org.springframework.boot.web.server.WebServer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import java.util.*;

@Component
public class RoomWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, List<WebSocketSession>> rooms = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        String roomCode = getRoomCode(session);
        rooms.computeIfAbsent(roomCode, k -> new ArrayList<>()).add(session);
        System.out.println("User joined room: " + roomCode);
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String roomCode = getRoomCode(session);
        for (WebSocketSession s : rooms.getOrDefault(roomCode, new ArrayList<>())) {
            if(s.isOpen() && !s.getId().equals(session.getId())){
                s.sendMessage(message);
            }
        }
    }

    private String getRoomCode(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1];
    }

}
