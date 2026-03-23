package com.ShareText.demo.services.RoomService;


import com.ShareText.demo.dto.CreateRoomRequest;
import com.ShareText.demo.enums.Expiry;
import com.ShareText.demo.models.Room;
import com.ShareText.demo.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    public RoomServiceImpl(RoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }


    @Override
    public String createRoom(CreateRoomRequest request) {

        String roomCode;

        // Generate unique room code
        do {
            roomCode = generateRoomCode();
        } while (roomRepository.existsByRoomCode(roomCode));

        LocalDateTime now = LocalDateTime.now();

        Room room = new Room();
        room.setRoomCode(roomCode);
        room.setContent(request.getContent());
        room.setLanguage(request.getLanguage().name());
        room.setCreatedAt(now);
        room.setExpiresAt(calculateExpiry(now, request.getExpiry()));
        room.setAccessControlled(false);

        roomRepository.save(room);

        return roomCode;
    }

    private LocalDateTime calculateExpiry(LocalDateTime now, Expiry expiry) {
        switch (expiry) {
            case ONE_HOUR:
                return now.plusHours(1);
            case ONE_DAY:
                return now.plusDays(1);
            case TWO_DAYS:
                return now.plusDays(2);
            case ONE_WEEK:
                return now.plusWeeks(1);
            case ONE_MONTH:
                return now.plusMonths(1);
            default:
                throw new IllegalArgumentException("Invalid expiry type");
        }
    }


    private String generateRoomCode() {

        StringBuilder code = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return code.toString();
    }

    @Override
    public Room getById(String roomCode){
        return this.roomRepository.findActiveRoom(roomCode, LocalDateTime.now()).orElse(null);
    }


    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 */2 * *")
    public void deleteExpiredRooms(){
        this.roomRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }


    @Override
    public void updateContentInRoom(String roomCode, String content){
        Room room = this.roomRepository.findByRoomCode(roomCode).orElse(null);

        if(room != null && !room.getContent().equals(content)){
            this.roomRepository.updateRoomContent(roomCode, content, LocalDateTime.now());
        }

    }

}
