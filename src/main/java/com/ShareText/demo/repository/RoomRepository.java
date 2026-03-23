package com.ShareText.demo.repository;

import com.ShareText.demo.models.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    Optional<Room> findByRoomCode(String roomCode);
    boolean existsByRoomCode(String roomCode);

    @Query("SELECT r FROM Room r WHERE r.roomCode = :roomCode AND r.expiresAt > :now")
    Optional<Room> findActiveRoom(@Param("roomCode") String roomCode, @Param("now") LocalDateTime now);

    void deleteByExpiresAtBefore(LocalDateTime now);

    @Transactional
    @Modifying
    @Query("UPDATE Room r Set r.content = :content WHERE r.roomCode = :roomCode AND r.expiresAt > :now")
    int updateRoomContent(@Param("roomCode") String roomCode, @Param("content") String content, @Param("now") LocalDateTime now);
}
