package com.ShareText.demo.controllers;


import com.ShareText.demo.dto.ApiResponse;
import com.ShareText.demo.dto.CreateRoomRequest;
import com.ShareText.demo.models.Room;
import com.ShareText.demo.services.RoomService.RoomService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoom( @Valid @RequestBody CreateRoomRequest request){
        System.out.println("Hello");
        String roomCode = roomService.createRoom(request);
        ApiResponse<String> response =
                new ApiResponse<>(201, roomCode, "Room created successfully");

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{roomCode}")
    public ResponseEntity<?> getById(@PathVariable("roomCode") String roomCode){
        Room room = this.roomService.getById(roomCode);
        if(room == null){
            ApiResponse<Object> response =
                    new ApiResponse<>(404, null, "Room not found");

            return ResponseEntity.status(404).body(response);
        }

        ApiResponse<Room> response =
                new ApiResponse<>(200, room, "Room fetched successfully");

        return ResponseEntity.ok(response);
    }

}
