package com.example.widgetbackend.integration;

import com.example.widgetbackend.user.User;
import com.example.widgetbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfileApiIntegrationTest {
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
        user.setPassword("password");
        user.setRole(com.example.widgetbackend.user.Role.USER);
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    void getProfile_asUser_returnsProfile() throws Exception {
        mockMvc.perform(get("/api/profile/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getProfile_unauthenticated_forbidden() throws Exception {
        mockMvc.perform(get("/api/profile/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
