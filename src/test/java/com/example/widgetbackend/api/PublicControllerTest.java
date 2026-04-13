package com.example.widgetbackend.api;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicControllerTest {
    @Test
    void ping_returnsOkStatusAndTimestamp() {
        PublicController controller = new PublicController();
        Map<String, Object> result = controller.ping();
        assertEquals("ok", result.get("status"));
        assertNotNull(result.get("timestamp"));
    }
}
