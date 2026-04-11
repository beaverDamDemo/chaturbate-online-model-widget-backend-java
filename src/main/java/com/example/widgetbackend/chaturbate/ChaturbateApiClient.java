package com.example.widgetbackend.chaturbate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Set;

@Service
public class ChaturbateApiClient {

    private static final Logger log = LoggerFactory.getLogger(ChaturbateApiClient.class);
    private static final String ROOM_STATUS_URL =
            "https://chaturbate.com/api/chatvideocontext/{room}/";
    private static final Set<String> ONLINE_STATUSES = Set.of("public", "private", "group");

    private final RestClient restClient;

    public ChaturbateApiClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    /**
     * Checks whether a single room is currently online on Chaturbate.
     * Uses the public chatvideocontext API — no token required.
     */
    public boolean isRoomOnline(String roomName) {
        try {
            RoomContext ctx = restClient.get()
                    .uri(ROOM_STATUS_URL, roomName)
                    .retrieve()
                    .body(RoomContext.class);
            if (ctx == null || ctx.roomStatus() == null) return false;
            return ONLINE_STATUSES.contains(ctx.roomStatus());
        } catch (RestClientException e) {
            log.warn("Failed to fetch status for room '{}': {}", roomName, e.getMessage());
            return false;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record RoomContext(@JsonProperty("room_status") String roomStatus) {}
}
