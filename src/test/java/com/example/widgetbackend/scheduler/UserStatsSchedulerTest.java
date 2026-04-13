package com.example.widgetbackend.scheduler;

import com.example.widgetbackend.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class UserStatsSchedulerTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserStatsScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scheduler = new UserStatsScheduler(userRepository);
    }

    @Test
    void reportUserCount_logsUserCount() {
        when(userRepository.count()).thenReturn(42L);
        scheduler.reportUserCount();
        verify(userRepository).count();
    }
}
