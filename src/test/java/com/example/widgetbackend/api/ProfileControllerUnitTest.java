package com.example.widgetbackend.api;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileControllerUnitTest {
    @Test
    void me_returnsUsername() {
        ProfileController controller = new ProfileController();
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");
        Map<String, String> result = controller.me(auth);
        assertEquals("testuser", result.get("username"));
    }
}
