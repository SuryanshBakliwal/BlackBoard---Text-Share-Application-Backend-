package com.ShareText.demo.WebSocket;


import com.ShareText.demo.repository.RoomRepository;
import com.ShareText.demo.services.RoomService.RoomService;
import com.ShareText.demo.services.RoomService.RoomServiceImpl;
import org.springframework.boot.web.server.WebServer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, List<WebSocketSession>> rooms = new HashMap<>();
    private final Map<String, String> roomBuffer = new ConcurrentHashMap<>();
    private final Set<String> dirtyRooms = ConcurrentHashMap.newKeySet();

    private final RoomService roomService;

    public RoomWebSocketHandler(RoomService roomService) {
        this.roomService = roomService;
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        String roomCode = getRoomCode(session);
        rooms.computeIfAbsent(roomCode, k -> new ArrayList<>()).add(session);
        System.out.println("User joined room: " + roomCode);
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        String roomCode = getRoomCode(session);
        String content = message.getPayload();

        roomBuffer.put(roomCode, content);
        dirtyRooms.add(roomCode);
        for (WebSocketSession s : rooms.getOrDefault(roomCode, new ArrayList<>())) {
            if(s.isOpen() && !s.getId().equals(session.getId())){
                s.sendMessage(message);
            }
        }
    }

    private String getRoomCode(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1];
    }


    @Scheduled(fixedRate = 5000)
    public void updateContentInRoom(){
        for(String room : dirtyRooms){
            String content = roomBuffer.get(room);

            if(content == null) continue;
            this.roomService.updateContentInRoom(room, content);
        }
    }

}
