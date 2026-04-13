package com.example.widgetbackend.api;

import com.example.widgetbackend.favorite.FavoriteRepository;
import com.example.widgetbackend.chaturbate.ChaturbateApiClient;
import com.example.widgetbackend.user.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final FavoriteRepository favoriteRepository;
    private final ChaturbateApiClient chaturbateApiClient;
    private final UserRepository userRepository;

    public DashboardController(FavoriteRepository favoriteRepository, ChaturbateApiClient chaturbateApiClient,
            UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.chaturbateApiClient = chaturbateApiClient;
        this.userRepository = userRepository;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        // Backend health
        String status = "live";

        // Active models: count of distinct rooms currently online
        var distinctRooms = favoriteRepository.findDistinctRoomNames();
        long activeModels = distinctRooms.stream().filter(chaturbateApiClient::isRoomOnline).count();

        // Online favorites: count of favorites marked online
        long onlineFavorites = favoriteRepository.findAll().stream().filter(f -> f.isOnline()).count();

        // Avg. response and conversion rate: placeholder/mock for now
        int avgResponseMs = 120; // TODO: wire real metric
        double conversionRate = 0.13; // TODO: wire real metric

        Map<String, Object> resp = new HashMap<>();
        resp.put("status", status);
        resp.put("activeModels", activeModels);
        resp.put("onlineFavorites", onlineFavorites);
        resp.put("avgResponseMs", avgResponseMs);
        resp.put("conversionRate", conversionRate);
        return resp;
    }
}
