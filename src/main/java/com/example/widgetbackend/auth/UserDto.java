package com.example.widgetbackend.auth;

public class UserDto {

    private final Long id;
    private final String email;
    private final String name;
    private final String role;

    public UserDto(Long id, String email, String name, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}
