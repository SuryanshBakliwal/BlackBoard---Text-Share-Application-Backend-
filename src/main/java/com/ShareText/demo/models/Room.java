package com.ShareText.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String roomCode;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean accessControlled;

    @Column(nullable = false)
    private String language;



    public Room(UUID id, String roomCode, UUID ownerId, String content, LocalDateTime createdAt, LocalDateTime expiresAt, boolean accessControlled, String language) {
        this.id = id;
        this.roomCode = roomCode;
        this.ownerId = ownerId;
        this.content = content;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.accessControlled = accessControlled;
        this.language = language;
    }

    public Room() {

    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isAccessControlled() {
        return accessControlled;
    }

    public void setAccessControlled(boolean accessControlled) {
        this.accessControlled = accessControlled;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
