package com.example.widgetbackend.admin;

import com.example.widgetbackend.user.Role;
import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

class AdminSeederTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AdminSeedProperties props;
    @InjectMocks
    private AdminSeeder seeder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seeder = new AdminSeeder(userRepository, passwordEncoder, props);
    }

    @Test
    void run_createsAdminIfNotExists() {
        when(props.getEmail()).thenReturn("admin@example.com");
        when(props.getPassword()).thenReturn("pass");
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        seeder.run();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void run_updatesRoleIfExistsAndNotAdmin() {
        User existing = new User("Admin", "admin@example.com", "encoded", Role.USER);
        when(props.getEmail()).thenReturn("admin@example.com");
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(existing));
        seeder.run();
        verify(userRepository).save(existing);
    }

    @Test
    void run_doesNothingIfNoEmail() {
        when(props.getEmail()).thenReturn("");
        seeder.run();
        verify(userRepository, never()).save(any());
    }
}
