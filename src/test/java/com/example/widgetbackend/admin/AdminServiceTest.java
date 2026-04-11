package com.example.widgetbackend.admin;

import com.example.widgetbackend.auth.UserDto;
import com.example.widgetbackend.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdminServiceTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;

    @Test
    void getAllUsers_returnsUserDtos() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<UserDto> users = adminService.getAllUsers();
        assertThat(users).isNotNull();
    }
}
