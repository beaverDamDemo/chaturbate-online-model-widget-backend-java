package com.example.widgetbackend.scheduler;

import com.example.widgetbackend.chaturbate.ChaturbateApiClient;
import com.example.widgetbackend.favorite.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;

class RoomOnlineStatusSchedulerTest {
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private ChaturbateApiClient chaturbateApiClient;
    @InjectMocks
    private RoomOnlineStatusScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scheduler = new RoomOnlineStatusScheduler(favoriteRepository, chaturbateApiClient);
    }

    @Test
    void syncRoomOnlineStatus_updatesOnlineAndOfflineRooms() {
        List<String> rooms = List.of("room1", "room2", "room3");
        when(favoriteRepository.findDistinctRoomNames()).thenReturn(rooms);
        when(chaturbateApiClient.isRoomOnline("room1")).thenReturn(true);
        when(chaturbateApiClient.isRoomOnline("room2")).thenReturn(false);
        when(chaturbateApiClient.isRoomOnline("room3")).thenReturn(true);

        scheduler.syncRoomOnlineStatus();

        ArgumentCaptor<List<String>> onlineCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<String>> offlineCaptor = ArgumentCaptor.forClass(List.class);
        verify(favoriteRepository).updateOnlineStatusForRooms(onlineCaptor.capture(), eq(true), any(Instant.class));
        verify(favoriteRepository).updateOnlineStatusForRooms(offlineCaptor.capture(), eq(false), any(Instant.class));

        List<String> onlineRooms = onlineCaptor.getAllValues().get(0);
        List<String> offlineRooms = offlineCaptor.getAllValues().get(0);
        assert onlineRooms.containsAll(List.of("room1", "room3"));
        assert offlineRooms.contains("room2");
    }

    @Test
    void syncRoomOnlineStatus_noRooms_nothingHappens() {
        when(favoriteRepository.findDistinctRoomNames()).thenReturn(List.of());
        scheduler.syncRoomOnlineStatus();
        verify(favoriteRepository, never()).updateOnlineStatusForRooms(anyList(), anyBoolean(), any());
    }
}
