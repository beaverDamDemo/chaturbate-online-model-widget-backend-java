package com.example.widgetbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WidgetBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WidgetBackendApplication.class, args);
    }
}
