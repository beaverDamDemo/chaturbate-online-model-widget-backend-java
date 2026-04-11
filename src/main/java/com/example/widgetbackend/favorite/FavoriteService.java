package com.example.widgetbackend.favorite;

import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
    }

    public List<FavoriteDto> getFavorites(String email) {
        User user = getUser(email);
        return favoriteRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    public FavoriteDto addFavorite(String email, String roomName) {
        User user = getUser(email);
        if (favoriteRepository.existsByUserIdAndRoomName(user.getId(), roomName)) {
            throw new IllegalArgumentException("Room is already in favorites");
        }
        Favorite favorite = favoriteRepository.save(new Favorite(user, roomName));
        return toDto(favorite);
    }

    @Transactional
    public void removeFavorite(String email, String roomName) {
        User user = getUser(email);
        if (!favoriteRepository.existsByUserIdAndRoomName(user.getId(), roomName)) {
            throw new IllegalArgumentException("Room not found in favorites");
        }
        favoriteRepository.deleteByUserIdAndRoomName(user.getId(), roomName);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    private FavoriteDto toDto(Favorite f) {
        return new FavoriteDto(f.getId(), f.getRoomName(), f.isOnline(), f.getStatusCheckedAt(), f.getCreatedAt());
    }
}
