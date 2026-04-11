package com.example.widgetbackend.admin;

import java.util.List;

public class AdminStatsDto {

    private long totalUsers;
    private long totalFavorites;
    private List<RoomStatsDto> roomStats;

    public AdminStatsDto(long totalUsers, long totalFavorites, List<RoomStatsDto> roomStats) {
        this.totalUsers = totalUsers;
        this.totalFavorites = totalFavorites;
        this.roomStats = roomStats;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public long getTotalFavorites() {
        return totalFavorites;
    }

    public List<RoomStatsDto> getRoomStats() {
        return roomStats;
    }

    public static class RoomStatsDto {
        private String roomName;
        private long favoriteCount;

        public RoomStatsDto(String roomName, long favoriteCount) {
            this.roomName = roomName;
            this.favoriteCount = favoriteCount;
        }

        public String getRoomName() {
            return roomName;
        }

        public long getFavoriteCount() {
            return favoriteCount;
        }
    }
}
