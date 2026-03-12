package com.ShareText.demo.services.RoomService;


import com.ShareText.demo.dto.CreateRoomRequest;
import com.ShareText.demo.models.Room;

public interface RoomService {

    String createRoom(CreateRoomRequest createRoomRequest);
    Room getById(String roomCode);
}
