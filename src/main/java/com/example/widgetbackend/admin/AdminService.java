package com.example.widgetbackend.admin;

import com.example.widgetbackend.chaturbate.ChaturbateApiClient;
import com.example.widgetbackend.favorite.FavoriteRepository;
import com.example.widgetbackend.user.Role;
import com.example.widgetbackend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final ChaturbateApiClient chaturbateApiClient;

    public AdminService(UserRepository userRepository, FavoriteRepository favoriteRepository,
            ChaturbateApiClient chaturbateApiClient) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.chaturbateApiClient = chaturbateApiClient;
    }

    public AdminStatsDto getStats() {
        long totalUsers = userRepository.countByRole(Role.USER);
        long totalFavorites = favoriteRepository.count();

        List<AdminStatsDto.RoomStatsDto> roomStats = favoriteRepository.findRoomStats()
                .stream()
                .map(r -> new AdminStatsDto.RoomStatsDto(r.getRoomName(), r.getFavoriteCount()))
                .toList();

        var distinctRooms = favoriteRepository.findDistinctRoomNames();
        long activeModels = distinctRooms.stream().filter(chaturbateApiClient::isRoomOnline).count();
        long onlineFavorites = favoriteRepository.findAll().stream().filter(f -> f.isOnline()).count();
        int avgResponseMs = 120;
        double conversionRate = 0.13;
        String status = "live";

        return new AdminStatsDto(totalUsers, totalFavorites, roomStats, status, activeModels, onlineFavorites,
                avgResponseMs, conversionRate);
    }

    // Returns all users as UserDto
    public List<com.example.widgetbackend.auth.UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new com.example.widgetbackend.auth.UserDto(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getRole().name()))
                .toList();
    }
}
