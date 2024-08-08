package com.sampak.gameapp.module;

import java.util.stream.Collectors;

import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.service.JwtService;
import com.sampak.gameapp.service.UserEntityServiceDetails;
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
    private UserEntityServiceDetails userDetailsService;

    public SocketModule(SocketIOServer server, JwtService jwtService, UserEntityServiceDetails userDetailsService) {
        this.server = server;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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
                client.set("userId", userId);
                log.info("Socket ID[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), userId);
                client.sendEvent("read_message", "connected");
            } else {
                client.disconnect();
            }
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            log.info("Socket ID[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), client.get("userId"));
        };
    }

}