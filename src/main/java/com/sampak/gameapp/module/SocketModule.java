package com.sampak.gameapp.module;

import java.net.Socket;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sampak.gameapp.dto.FriendSocket;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.service.JwtService;
import com.sampak.gameapp.service.SocketService;
import com.sampak.gameapp.service.impl.JwtServiceImpl;
import com.sampak.gameapp.service.impl.UserEntityServiceDetailsImpl;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SocketModule {

    private final SocketIOServer server;
    private JwtService jwtService;
    private UserEntityServiceDetailsImpl userDetailsService;
    private SocketService socketService;

    public SocketModule(SocketIOServer server, JwtService jwtService, UserEntityServiceDetailsImpl userDetailsService, SocketService socketService) {
        this.server = server;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.socketService = socketService;
        server.addConnectListener(this.onConnected());
        server.addDisconnectListener(this.onDisconnected());
    }

    private ConnectListener onConnected() {
        return (client) -> {
            var params = client.getHandshakeData().getUrlParams();
            String token = params.get("token").stream().collect(Collectors.joining());
            String userId = jwtService.extractUsername(token);
            UserEntity userDetails = userDetailsService.loadUserById(userId);
            if(userId != null && jwtService.validateToken(token, userDetails)) {
                if(client.get("userId") != null) { // If user is connected to socket then don't try to send events
                    if( client.get("userId").equals(userId)) {
                        return;
                    }
                }
                client.set("userId", userId);
                socketService.notifyFriendsOnlineStatusChange(UUID.fromString(userId), true);
                log.info("Socket ID[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), userId);
            } else {
                client.disconnect();
            }
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            String userId = client.get("userId");

            if (userId != null) {
                socketService.notifyFriendsOnlineStatusChange(UUID.fromString(userId), false);
            }

            log.info("Socket ID[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), client.get("userId"));
        };
    }



}