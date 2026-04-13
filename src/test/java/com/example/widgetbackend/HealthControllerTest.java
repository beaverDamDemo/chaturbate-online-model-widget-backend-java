package com.example.widgetbackend;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {
    @Test
    void health_returnsLiveStatus() {
        HealthController controller = new HealthController();
        ResponseEntity<Map<String, String>> response = controller.health();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("live", response.getBody().get("status"));
    }
}
