package com.example.widgetbackend.admin;

import com.example.widgetbackend.user.Role;
import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import com.example.widgetbackend.favorite.Favorite;
import com.example.widgetbackend.favorite.FavoriteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminSeedProperties props;
    private final FavoriteRepository favoriteRepository;

    public AdminSeeder(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AdminSeedProperties props,
            FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.props = props;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public void run(String... args) {
        if (props.getEmail() == null || props.getEmail().isBlank()) {
            return;
        }
        userRepository.findByEmail(props.getEmail()).ifPresentOrElse(
                existing -> {
                    if (existing.getRole() != Role.ADMIN) {
                        existing.setRole(Role.ADMIN);
                        userRepository.save(existing);
                    }
                    seedFavorites(existing);
                },
                () -> {
                    User admin = new User(
                            "Admin",
                            props.getEmail(),
                            passwordEncoder.encode(props.getPassword()),
                            Role.ADMIN);
                    User savedAdmin = userRepository.save(admin);
                    seedFavorites(savedAdmin);
                });

    }

    private void seedFavorites(User admin) {
        // Only add if not already present
        String[] rooms = { "lil_uff", "misumi_and_jason", "myassistant" };
        for (String room : rooms) {
            if (!favoriteRepository.existsByUserIdAndRoomName(admin.getId(), room)) {
                favoriteRepository.save(new com.example.widgetbackend.favorite.Favorite(admin, room));
            }
        }
    }
}
