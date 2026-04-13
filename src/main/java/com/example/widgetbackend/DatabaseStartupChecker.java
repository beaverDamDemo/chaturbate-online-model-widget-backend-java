package com.example.widgetbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class DatabaseStartupChecker implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseStartupChecker(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            jdbcTemplate.execute("SELECT 1");
        } catch (Exception ex) {
            // ANSI escape code for red foreground
            String ANSI_RESET = "\u001B[0m";
            String ANSI_RED = "\u001B[31m";
            String warning = "\n" +
                    ANSI_RED +
                    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                    "!!!  WARNING: DATABASE IS NOT RUNNING!                    !!!\n" +
                    "!!!  Please start the database container before backend.   !!!\n" +
                    "!!!  For example: docker-compose up -d db                 !!!\n" +
                    "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n" +
                    ANSI_RESET +
                    "\n\n";
            // Print the warning multiple times for visibility
            for (int i = 0; i < 3; i++) {
                System.out.println(warning);
            }
        }
    }
}