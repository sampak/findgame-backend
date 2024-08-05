package com.sampak.gameapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GameappApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameappApplication.class, args);
    }

}
