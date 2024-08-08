package com.sampak.gameapp.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.sampak.gameapp.service.SocketService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SocketServiceImpl implements SocketService {

    private SocketIOServer server;

    public SocketServiceImpl(SocketIOServer server) {
        this.server = server;
    }

    public SocketIOClient findClientByUserId(UUID userId) {
        for (SocketIOClient client : server.getAllClients()) {
            String clientUserIdStr = client.get("userId");
            if (clientUserIdStr != null) {
                UUID clientUserId = UUID.fromString(clientUserIdStr);
                if (clientUserId.equals(userId)) {
                    System.out.println("Znalazłem clienta");
                    return client;
                }
            }
        }
        return null;
    }

    public <T> void sendMessage(UUID userId, String eventName, T message) {
        SocketIOClient client = findClientByUserId(userId);
        if (client == null) {
            return;
        }

        client.sendEvent(eventName, message);
    }
}
