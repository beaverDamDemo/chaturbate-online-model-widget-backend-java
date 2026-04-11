package com.example.widgetbackend.favorite;

import com.example.widgetbackend.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "favorites", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "room_name" })
})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(nullable = false)
    private boolean online = false;

    @Column(name = "status_checked_at")
    private Instant statusCheckedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Favorite() {
    }

    public Favorite(User user, String roomName) {
        this.user = user;
        this.roomName = roomName;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Instant getStatusCheckedAt() {
        return statusCheckedAt;
    }

    public void setStatusCheckedAt(Instant statusCheckedAt) {
        this.statusCheckedAt = statusCheckedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
