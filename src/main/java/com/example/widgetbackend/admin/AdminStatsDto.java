package com.example.widgetbackend.admin;

import java.util.List;

public class AdminStatsDto {

    private long totalUsers;
    private long totalFavorites;
    private List<RoomStatsDto> roomStats;
    private String status;
    private long activeModels;
    private long onlineFavorites;
    private int avgResponseMs;
    private double conversionRate;

    public AdminStatsDto(long totalUsers, long totalFavorites, List<RoomStatsDto> roomStats,
            String status, long activeModels, long onlineFavorites, int avgResponseMs,
            double conversionRate) {
        this.totalUsers = totalUsers;
        this.totalFavorites = totalFavorites;
        this.roomStats = roomStats;
        this.status = status;
        this.activeModels = activeModels;
        this.onlineFavorites = onlineFavorites;
        this.avgResponseMs = avgResponseMs;
        this.conversionRate = conversionRate;
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

    public String getStatus() {
        return status;
    }

    public long getActiveModels() {
        return activeModels;
    }

    public long getOnlineFavorites() {
        return onlineFavorites;
    }

    public int getAvgResponseMs() {
        return avgResponseMs;
    }

    public double getConversionRate() {
        return conversionRate;
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
