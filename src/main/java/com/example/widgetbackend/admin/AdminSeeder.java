package com.example.widgetbackend.admin;

import com.example.widgetbackend.user.Role;
import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
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

    public AdminSeeder(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AdminSeedProperties props) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.props = props;
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
                },
                () -> {
                    User admin = new User(
                            "Admin",
                            props.getEmail(),
                            passwordEncoder.encode(props.getPassword()),
                            Role.ADMIN);
                    userRepository.save(admin);
                });
    }
}
