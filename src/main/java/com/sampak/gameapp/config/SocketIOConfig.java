package com.sampak.gameapp.config;

import com.corundumstudio.socketio.AckMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOServer;

@Configuration
public class SocketIOConfig {

//    @Value("${socket-server.host}")
    private String host = "127.0.0.1";

//    @Value("${socket-server.port}")
    private Integer port = 5000;

    @Bean
    public SocketIOServer socketIOServer() {

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setAckMode(AckMode.AUTO);


        return new SocketIOServer(config);
    }

}