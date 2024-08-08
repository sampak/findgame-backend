package com.sampak.gameapp.service;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.List;
import java.util.UUID;

public interface SocketService {
    List<UUID> getOnlineFriends(UUID userId);
    SocketIOClient findClientByUserId(UUID userId);
    <T> void sendMessage(UUID userId, String eventName, T message);
    void notifyFriendsOnlineStatusChange(UUID userId, boolean isOnline);
}
