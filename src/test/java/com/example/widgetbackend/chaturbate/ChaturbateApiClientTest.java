package com.example.widgetbackend.chaturbate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChaturbateApiClientTest {
    @Mock
    private RestClient.Builder builder;
    @Mock
    private RestClient restClient;
    @InjectMocks
    private ChaturbateApiClient apiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(builder.build()).thenReturn(restClient);
        apiClient = new ChaturbateApiClient(builder);
    }

    @Test
    void isRoomOnline_returnsTrueForOnlineStatus() {
        ChaturbateApiClient.RoomContext ctx = new ChaturbateApiClient.RoomContext("public");
        var request = mock(RestClient.RequestHeadersUriSpec.class);
        var requestSpec = mock(RestClient.RequestHeadersSpec.class);
        var responseSpec = mock(RestClient.ResponseSpec.class);
        when(restClient.get()).thenReturn(request);
        when(request.uri(anyString(), (Object[]) any())).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(ChaturbateApiClient.RoomContext.class)).thenReturn(ctx);
        assertTrue(apiClient.isRoomOnline("room1"));
    }

    @Test
    void isRoomOnline_returnsFalseForOfflineStatus() {
        ChaturbateApiClient.RoomContext ctx = new ChaturbateApiClient.RoomContext("offline");
        var request = mock(RestClient.RequestHeadersUriSpec.class);
        var requestSpec = mock(RestClient.RequestHeadersSpec.class);
        var responseSpec = mock(RestClient.ResponseSpec.class);
        when(restClient.get()).thenReturn(request);
        when(request.uri(anyString(), (Object[]) any())).thenReturn(requestSpec);
        when(requestSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(ChaturbateApiClient.RoomContext.class)).thenReturn(ctx);
        assertFalse(apiClient.isRoomOnline("room2"));
    }

    @Test
    void isRoomOnline_handlesRestClientException() {
        when(restClient.get()).thenThrow(new RestClientException("error"));
        assertFalse(apiClient.isRoomOnline("room3"));
    }
}
