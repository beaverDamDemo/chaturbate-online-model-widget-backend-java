package com.example.widgetbackend.admin;

import com.example.widgetbackend.favorite.Favorite;
import com.example.widgetbackend.favorite.FavoriteRepository;
import com.example.widgetbackend.user.Role;
import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
@Order(2)
public class DevDataSeeder implements CommandLineRunner {

    private static final String DUMMY_EMAIL_MARKER = "dummyuser";

    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDataSeeder(UserRepository userRepository,
            FavoriteRepository favoriteRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Skip if dummy users already exist
        if (userRepository.existsByEmail("dummyuser1@example.com")) {
            return;
        }

        String encodedPassword = passwordEncoder.encode("password");

        List<User> users = List.of(
                new User("Alice", "dummyuser1@example.com", encodedPassword, Role.USER),
                new User("Bob", "dummyuser2@example.com", encodedPassword, Role.USER),
                new User("Charlie", "dummyuser3@example.com", encodedPassword, Role.USER),
                new User("Diana", "dummyuser4@example.com", encodedPassword, Role.USER),
                new User("Eve", "dummyuser5@example.com", encodedPassword, Role.USER));
        userRepository.saveAll(users);

        List<String> rooms = List.of("roomA", "roomB", "roomC", "roomD", "roomE");

        // Alice → roomA, roomB, roomC
        saveFavorites(users.get(0), List.of("roomA", "roomB", "roomC"));
        // Bob → roomA, roomB
        saveFavorites(users.get(1), List.of("roomA", "roomB"));
        // Charlie → roomA, roomC, roomD
        saveFavorites(users.get(2), List.of("roomA", "roomC", "roomD"));
        // Diana → roomB, roomD, roomE
        saveFavorites(users.get(3), List.of("roomB", "roomD", "roomE"));
        // Eve → roomC, roomE
        saveFavorites(users.get(4), List.of("roomC", "roomE"));
    }

    private void saveFavorites(User user, List<String> roomNames) {
        for (String roomName : roomNames) {
            favoriteRepository.save(new Favorite(user, roomName));
        }
    }
}
