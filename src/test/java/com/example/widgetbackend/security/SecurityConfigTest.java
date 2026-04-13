package com.example.widgetbackend.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {
    @Test
    void passwordEncoder_returnsBCryptPasswordEncoder() {
        SecurityConfig config = new SecurityConfig(null, null);
        PasswordEncoder encoder = config.passwordEncoder();
        assertTrue(encoder instanceof BCryptPasswordEncoder);
        String raw = "password";
        String encoded = encoder.encode(raw);
        assertTrue(encoder.matches(raw, encoded));
    }
}
