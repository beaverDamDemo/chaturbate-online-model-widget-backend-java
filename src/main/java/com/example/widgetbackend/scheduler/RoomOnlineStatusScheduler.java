package com.example.widgetbackend.scheduler;

import com.example.widgetbackend.chaturbate.ChaturbateApiClient;
import com.example.widgetbackend.favorite.FavoriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
public class RoomOnlineStatusScheduler {

    private static final Logger log = LoggerFactory.getLogger(RoomOnlineStatusScheduler.class);

    private final FavoriteRepository favoriteRepository;
    private final ChaturbateApiClient chaturbateApiClient;

    public RoomOnlineStatusScheduler(FavoriteRepository favoriteRepository,
                                     ChaturbateApiClient chaturbateApiClient) {
        this.favoriteRepository = favoriteRepository;
        this.chaturbateApiClient = chaturbateApiClient;
    }

    @Scheduled(fixedRate = 600_000, initialDelay = 10_000)
    @Transactional
    public void syncRoomOnlineStatus() {
        List<String> distinctRooms = favoriteRepository.findDistinctRoomNames();
        if (distinctRooms.isEmpty()) {
            return;
        }

        log.info("Syncing online status for {} distinct rooms", distinctRooms.size());

        List<String> nowOnline  = distinctRooms.stream().filter(chaturbateApiClient::isRoomOnline).toList();
        List<String> nowOffline = distinctRooms.stream().filter(r -> !nowOnline.contains(r)).toList();

        Instant checkedAt = Instant.now();
        if (!nowOnline.isEmpty())  favoriteRepository.updateOnlineStatusForRooms(nowOnline,  true,  checkedAt);
        if (!nowOffline.isEmpty()) favoriteRepository.updateOnlineStatusForRooms(nowOffline, false, checkedAt);

        log.info("Room sync complete: {} online, {} offline", nowOnline.size(), nowOffline.size());
    }
}
