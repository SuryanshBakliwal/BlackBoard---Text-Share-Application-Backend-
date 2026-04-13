package com.ShareText.demo.WebSocket;


import com.ShareText.demo.repository.RoomRepository;
import com.ShareText.demo.services.RoomService.RoomService;
import com.ShareText.demo.services.RoomService.RoomServiceImpl;
import org.springframework.boot.web.server.WebServer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.core.StringRedisTemplate;

@Component
public class RoomWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, List<WebSocketSession>> rooms = new ConcurrentHashMap<>();


    private final StringRedisTemplate redisTemplate;
    private final Map<String, Timer> roomTimers = new ConcurrentHashMap<>();
    private final Map<String, String> roomBuffer = new ConcurrentHashMap<>();
    private final Set<String> dirtyRooms = ConcurrentHashMap.newKeySet();

    private final RoomService roomService;

    public RoomWebSocketHandler(StringRedisTemplate redisTemplate, RoomService roomService) {
        this.redisTemplate = redisTemplate;
        this.roomService = roomService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomCode = getRoomCode(session);

        rooms.computeIfAbsent(roomCode,
                        k -> Collections.synchronizedList(new ArrayList<>()))
                .add(session);

        System.out.println("User joined room: " + roomCode);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String roomCode = getRoomCode(session);

        saveToDB(roomCode);
        List<WebSocketSession> sessions = rooms.get(roomCode);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                rooms.remove(roomCode);
            }
        }
    }


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomCode = getRoomCode(session);
        String content = message.getPayload();

        //store in redis
        redisTemplate.opsForValue().set("room:" + roomCode, content, Duration.ofMinutes(10));

        //debounce DB save
        if(roomTimers.containsKey(roomCode)){
            roomTimers.get(roomCode).cancel();
        }
        Timer timer = new Timer();

        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                saveToDB(roomCode);
            }
        }, 3000);

        // 🔥 3. Broadcast to others
        for (WebSocketSession s : rooms.getOrDefault(roomCode, new ArrayList<>())) {
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                s.sendMessage(message);
                System.out.println("Hello message getting broadcast");
            }
        }

    }

    private String getRoomCode(WebSocketSession session) {
        return session.getUri().getQuery().split("=")[1];
    }


    private void saveToDB(String roomCode) {
        String content = redisTemplate.opsForValue().get("room:" + roomCode);

        if (content != null) {
            roomService.updateContentInRoom(roomCode, content);
            System.out.println("Save to DB : " + roomCode);
        }
    }


    // without redis
//    @Scheduled(fixedRate = 5000)
//    public void updateContentInRoom() {
//        for (String room : dirtyRooms) {
//            String content = roomBuffer.get(room);
//
//            if (content == null) continue;
//            this.roomService.updateContentInRoom(room, content);
//            dirtyRooms.remove(room);
//        }
//    }

}
