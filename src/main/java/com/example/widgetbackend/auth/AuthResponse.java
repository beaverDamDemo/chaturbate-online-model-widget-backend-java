package com.example.widgetbackend.auth;

public class AuthResponse {

    private final String accessToken;
    private final UserDto user;

    public AuthResponse(String accessToken, UserDto user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public UserDto getUser() {
        return user;
    }
}
