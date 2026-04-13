package com.example.widgetbackend.admin;

import com.example.widgetbackend.favorite.Favorite;
import com.example.widgetbackend.favorite.FavoriteRepository;
import com.example.widgetbackend.user.Role;
import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
class DevDataSeederTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private DevDataSeeder seeder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seeder = new DevDataSeeder(userRepository, favoriteRepository, passwordEncoder);
    }

    @Test
    void run_skipsIfDummyUsersExist() {
        when(userRepository.existsByEmail("dummyuser1@example.com")).thenReturn(true);
        seeder.run();
        verify(userRepository, never()).saveAll(anyList());
    }

    @Test
    void run_createsDummyUsersAndFavorites() {
        when(userRepository.existsByEmail("dummyuser1@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        seeder.run();
        verify(userRepository).saveAll(anyList());
        verify(favoriteRepository, atLeastOnce()).save(any(Favorite.class));
    }
}
