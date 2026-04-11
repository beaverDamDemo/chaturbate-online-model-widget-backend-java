package com.example.widgetbackend.favorite;

import java.time.Instant;

public class FavoriteDto {

    private Long id;
    private String roomName;
    private boolean online;
    private Instant statusCheckedAt;
    private Instant createdAt;

    public FavoriteDto(Long id, String roomName, boolean online, Instant statusCheckedAt, Instant createdAt) {
        this.id = id;
        this.roomName = roomName;
        this.online = online;
        this.statusCheckedAt = statusCheckedAt;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean isOnline() {
        return online;
    }

    public Instant getStatusCheckedAt() {
        return statusCheckedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
