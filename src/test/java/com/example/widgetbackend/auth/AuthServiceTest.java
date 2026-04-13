package com.example.widgetbackend.auth;

import com.example.widgetbackend.favorite.FavoriteRepository;
import com.example.widgetbackend.user.Role;
import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import com.example.widgetbackend.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private FavoriteRepository favoriteRepository;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService(userRepository, passwordEncoder, jwtService, authenticationManager,
                userDetailsService, favoriteRepository);
    }

    @Test
    void login_throwsIfUserNotFoundAfterAuth() {
        LoginRequest req = new LoginRequest();
        req.setEmail("nouser@example.com");
        req.setPassword("pass");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail("nouser@example.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authService.login(req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User not found after authentication");
    }

    @Test
    void me_throwsIfUserNotFound() {
        when(userRepository.findByEmail("nouser@example.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authService.me("nouser@example.com"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Authenticated user not found");
    }

    @Test
    void register_throwsIfEmailExists() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("exists@example.com");
        when(userRepository.existsByEmail("exists@example.com")).thenReturn(true);
        assertThatThrownBy(() -> authService.register(req))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("already in use");
    }

    @Test
    void deleteAccount_throwsIfUserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authService.deleteAccount("missing@example.com", "pass"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Authenticated user not found");
    }

    @Test
    void deleteAccount_throwsIfPasswordWrong() {
        User user = new User("Test User", "test@example.com", "encoded", Role.USER);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);
        assertThatThrownBy(() -> authService.deleteAccount("test@example.com", "wrong"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Incorrect password");
    }
}
