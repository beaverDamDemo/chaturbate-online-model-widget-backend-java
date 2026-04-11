package com.example.widgetbackend.favorite;

import jakarta.validation.constraints.NotBlank;

public class AddFavoriteRequest {

    @NotBlank
    private String roomName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
