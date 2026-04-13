package com.example.widgetbackend.auth;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserDtoTest {
    @Test
    void userDto_fieldsAreSetCorrectly() {
        UserDto dto = new UserDto(1L, "test@example.com", "Test User", "ADMIN");
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
        assertThat(dto.getName()).isEqualTo("Test User");
        assertThat(dto.getRole()).isEqualTo("ADMIN");
    }
}
