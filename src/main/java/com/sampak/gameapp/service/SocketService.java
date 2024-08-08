package com.sampak.gameapp.service;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.UUID;

public interface SocketService {
    SocketIOClient findClientByUserId(UUID userId);
    <T> void sendMessage(UUID userId, String eventName, T message);
}
