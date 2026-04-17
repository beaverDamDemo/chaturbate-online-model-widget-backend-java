package com.example.widgetbackend.favorite;

import com.example.widgetbackend.chaturbate.ChaturbateApiClient;
import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ChaturbateApiClient chaturbateApiClient;

    private static final Duration CACHE_DURATION = Duration.ofMinutes(10);

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository,
            ChaturbateApiClient chaturbateApiClient) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.chaturbateApiClient = chaturbateApiClient;
    }

    public List<FavoriteDto> getFavorites(String email) {
        User user = getUser(email);
        List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());

        // Check and update cache for each favorite if needed
        for (Favorite favorite : favorites) {
            if (shouldRefreshStatus(favorite)) {
                updateFavoriteStatus(favorite);
            }
        }

        return favorites.stream()
                .map(this::toDto)
                .toList();
    }

    private boolean shouldRefreshStatus(Favorite favorite) {
        if (favorite.getStatusCheckedAt() == null) {
            return true;
        }
        return Duration.between(favorite.getStatusCheckedAt(), Instant.now()).compareTo(CACHE_DURATION) > 0;
    }

    @Transactional
    private void updateFavoriteStatus(Favorite favorite) {
        boolean isOnline = chaturbateApiClient.isRoomOnline(favorite.getRoomName());
        favorite.setOnline(isOnline);
        favorite.setStatusCheckedAt(Instant.now());
        favoriteRepository.save(favorite);
    }

    public FavoriteDto addFavorite(String email, String roomName) {
        User user = getUser(email);
        if (favoriteRepository.existsByUserIdAndRoomName(user.getId(), roomName)) {
            throw new IllegalArgumentException("Room is already in favorites");
        }
        Favorite favorite = new Favorite(user, roomName);
        // Set initial status
        updateFavoriteStatus(favorite);
        favorite = favoriteRepository.save(favorite);
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
