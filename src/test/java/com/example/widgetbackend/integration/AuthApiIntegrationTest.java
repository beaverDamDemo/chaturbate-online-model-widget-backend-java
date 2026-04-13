package com.example.widgetbackend.integration;

import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthApiIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setName("user");
        user.setEmail("user@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setRole(com.example.widgetbackend.user.Role.USER);
        userRepository.save(user);
    }

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        String loginJson = "{\"email\":\"user@example.com\",\"password\":\"password\"}";
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void login_withInvalidCredentials_unauthorized() throws Exception {
        String loginJson = "{\"email\":\"user@example.com\",\"password\":\"wrongpass\"}";
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson))
                .andExpect(status().isForbidden());
    }
}
