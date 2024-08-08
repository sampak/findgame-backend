package com.sampak.gameapp.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.sampak.gameapp.dto.FriendSocket;
import com.sampak.gameapp.entity.FriendEntity;
import com.sampak.gameapp.entity.FriendStatus;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.repository.FriendRepository;
import com.sampak.gameapp.repository.UserRepository;
import com.sampak.gameapp.service.FriendService;
import com.sampak.gameapp.service.SocketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SocketServiceImpl implements SocketService {

    private SocketIOServer server;
    private FriendRepository friendRepository;
    private UserRepository userRepository;

    public SocketServiceImpl(SocketIOServer server, FriendRepository friendRepository, UserRepository userRepository) {
        this.server = server;
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;


    }

    public List<UUID> getOnlineFriends(UUID userId) {
        UserEntity user = userRepository.getById(userId);
        List<FriendEntity> friends = friendRepository.findByUserOrFriendAndStatus(user, user, FriendStatus.FRIENDS);

        List<UUID> friendIds = friends.stream()
                .map(friend -> friend.getUser().getId().equals(userId) ? friend.getFriend().getId() : friend.getUser().getId())
                .collect(Collectors.toList());

        List<UUID> onlineFriends = friendIds.stream()
                .filter(friendId -> findClientByUserId(friendId) != null)
                .collect(Collectors.toList());


        return onlineFriends;
    }


    public SocketIOClient findClientByUserId(UUID userId) {
        for (SocketIOClient client : server.getAllClients()) {
            String clientUserIdStr = client.get("userId");
            if (clientUserIdStr != null) {
                UUID clientUserId = UUID.fromString(clientUserIdStr);
                if (clientUserId.equals(userId)) {
                    return client;
                }
            }
        }
        return null;
    }

    public void notifyFriendsOnlineStatusChange(UUID userId, boolean isOnline) {
        List<UUID> friends = getOnlineFriends(userId);
        for (UUID friendId : friends) {
            sendMessage(friendId, isOnline ? FriendSocket.FRIEND_ONLINE.toString() : FriendSocket.FRIEND_OFFLINE.toString(), userId);
        }
    }

    public <T> void sendMessage(UUID userId, String eventName, T message) {
        SocketIOClient client = findClientByUserId(userId);
        if (client == null) {
            return;
        }

        client.sendEvent(eventName, message);
    }
}
