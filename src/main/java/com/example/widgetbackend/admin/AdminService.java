package com.example.widgetbackend.admin;

import com.example.widgetbackend.favorite.FavoriteRepository;
import com.example.widgetbackend.user.Role;
import com.example.widgetbackend.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    public AdminService(UserRepository userRepository, FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public AdminStatsDto getStats() {
        long totalUsers = userRepository.countByRole(Role.USER);
        long totalFavorites = favoriteRepository.count();

        List<AdminStatsDto.RoomStatsDto> roomStats = favoriteRepository.findRoomStats()
                .stream()
                .map(r -> new AdminStatsDto.RoomStatsDto(r.getRoomName(), r.getFavoriteCount()))
                .toList();

        return new AdminStatsDto(totalUsers, totalFavorites, roomStats);
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
