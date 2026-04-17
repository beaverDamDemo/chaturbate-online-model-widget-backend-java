package com.example.widgetbackend.favorite;

import com.example.widgetbackend.chaturbate.ChaturbateApiClient;
import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteServiceTest {
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChaturbateApiClient chaturbateApiClient;
    @InjectMocks
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFavorites_returnsEmptyListForUnknownUser() {
        Mockito.when(userRepository.findByEmail("unknown@example.com")).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> favoriteService.getFavorites("unknown@example.com"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void addFavorite_throwsIfUserNotFound() {
        Mockito.when(userRepository.findByEmail("nouser@example.com")).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> favoriteService.addFavorite("nouser@example.com", "room1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Authenticated user not found");
    }

    @Test
    void removeFavorite_throwsIfUserNotFound() {
        Mockito.when(userRepository.findByEmail("nouser@example.com")).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> favoriteService.removeFavorite("nouser@example.com", "room1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Authenticated user not found");
    }

    @Test
    void addFavorite_throwsIfRoomNameNull() {
        User user = new User("Test User", "test@example.com", "password", com.example.widgetbackend.user.Role.USER);
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        assertThatThrownBy(() -> favoriteService.addFavorite("test@example.com", null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void addFavorite_throwsIfAlreadyExists() {
        User user = new User("Test User", "test@example.com", "password", com.example.widgetbackend.user.Role.USER);
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        Mockito.when(favoriteRepository.existsByUserIdAndRoomName(user.getId(), "room1")).thenReturn(true);
        assertThatThrownBy(() -> favoriteService.addFavorite("test@example.com", "room1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already in favorites");
    }

    @Test
    void removeFavorite_throwsIfNotExists() {
        User user = new User("Test User", "test@example.com", "password", com.example.widgetbackend.user.Role.USER);
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(java.util.Optional.of(user));
        Mockito.when(favoriteRepository.existsByUserIdAndRoomName(user.getId(), "room1")).thenReturn(false);
        assertThatThrownBy(() -> favoriteService.removeFavorite("test@example.com", "room1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found in favorites");
    }
}
