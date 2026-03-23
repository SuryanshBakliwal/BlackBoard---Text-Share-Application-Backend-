package com.ShareText.demo.services.RoomService;


import com.ShareText.demo.dto.CreateRoomRequest;
import com.ShareText.demo.models.Room;

import java.time.LocalDateTime;

public interface RoomService {

    String createRoom(CreateRoomRequest createRoomRequest);
    Room getById(String roomCode);
    void deleteExpiredRooms();

    void updateContentInRoom(String room, String content);
}
