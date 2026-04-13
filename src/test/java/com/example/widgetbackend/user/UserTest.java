package com.example.widgetbackend.user;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void user_fieldsAreSetCorrectly() {
        User user = new User("Test User", "test@example.com", "password", Role.ADMIN);
        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
    }
}
