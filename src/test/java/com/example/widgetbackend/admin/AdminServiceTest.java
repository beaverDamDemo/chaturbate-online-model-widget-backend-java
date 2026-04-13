package com.example.widgetbackend.admin;

import com.example.widgetbackend.auth.UserDto;
import com.example.widgetbackend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_returnsUserDtos() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<UserDto> users = adminService.getAllUsers();
        assertThat(users).isNotNull();
    }
}
