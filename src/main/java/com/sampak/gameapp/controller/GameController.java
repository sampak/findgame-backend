package com.sampak.gameapp.controller;

import com.sampak.gameapp.dto.requests.FetchSteamGamesDTO;
import com.sampak.gameapp.dto.responses.ResponseSteamIdDTO;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.providers.CurrentUserProvider.CurrentUserProvider;
import com.sampak.gameapp.service.GameService;
import com.sampak.gameapp.service.SteamService;
import com.sampak.gameapp.service.impl.GameServiceImpl;
import com.sampak.gameapp.service.impl.SteamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {


    @Autowired
    GameService gameService;

    @Autowired
    SteamService steamService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @PostMapping("/steam")
    public ResponseSteamIdDTO fetchUserGamesFromSteam(@RequestBody FetchSteamGamesDTO fetchSteamGamesDTO) {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        String steamId = steamService.getSteamId(fetchSteamGamesDTO.getSteamName());
        gameService.fetchUserGamesFromSteam(user, steamId);

        return new ResponseSteamIdDTO(steamId);
    }
}
