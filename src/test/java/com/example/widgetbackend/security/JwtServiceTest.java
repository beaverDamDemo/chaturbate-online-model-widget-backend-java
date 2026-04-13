package com.example.widgetbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {
    @InjectMocks
    private JwtService jwtService;

    private final String secret = Base64.getEncoder()
            .encodeToString("testsecretkeytestsecretkeytestsecretkey12".getBytes());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService();
        // Use reflection to set private fields
        try {
            var secretField = JwtService.class.getDeclaredField("jwtSecret");
            secretField.setAccessible(true);
            secretField.set(jwtService, secret);
            var expField = JwtService.class.getDeclaredField("jwtExpirationMs");
            expField.setAccessible(true);
            expField.set(jwtService, 10000L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void generateAndValidateToken() {
        UserDetails user = User.withUsername("user@example.com").password("pass").roles("USER").build();
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, user));
        assertEquals("user@example.com", jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_returnsFalseForWrongUser() {
        UserDetails user = User.withUsername("user@example.com").password("pass").roles("USER").build();
        String token = jwtService.generateToken(user);
        UserDetails other = User.withUsername("other@example.com").password("pass").roles("USER").build();
        assertFalse(jwtService.isTokenValid(token, other));
    }

    @Test
    void isTokenValid_returnsFalseForExpiredToken() {
        UserDetails user = User.withUsername("user@example.com").password("pass").roles("USER").build();
        try {
            var expField = JwtService.class.getDeclaredField("jwtExpirationMs");
            expField.setAccessible(true);
            expField.set(jwtService, -1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String token = jwtService.generateToken(user);
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, user));
    }
}
