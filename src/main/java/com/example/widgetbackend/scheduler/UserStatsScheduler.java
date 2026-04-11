package com.example.widgetbackend.scheduler;

import com.example.widgetbackend.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserStatsScheduler {

    private static final Logger log = LoggerFactory.getLogger(UserStatsScheduler.class);

    private final UserRepository userRepository;

    public UserStatsScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void reportUserCount() {
        long totalUsers = userRepository.count();
        log.info("Scheduled user count report: {} users", totalUsers);
    }
}
