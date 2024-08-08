package com.sampak.gameapp.config;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerCommandLineRunner implements CommandLineRunner, DisposableBean {

    private final SocketIOServer server;

    @Override
    public void run(String... args) throws Exception {
        server.start();
    }

    @Override
    public void destroy() throws Exception {
        server.stop();
    }
}