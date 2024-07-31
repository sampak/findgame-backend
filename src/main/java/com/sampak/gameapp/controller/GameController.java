package com.sampak.gameapp.controller;

import com.sampak.gameapp.dto.requests.FetchSteamGamesDTO;
import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.providers.CurrentUserProvider.CurrentUserProvider;
import com.sampak.gameapp.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {


    @Autowired
    GameService gameService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @PostMapping("/steam")
    public List<GamesResponseDTO> fetchUserGamesFromSteam(@RequestBody FetchSteamGamesDTO fetchSteamGamesDTO) {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return gameService.fetchUserGamesFromSteam(user, fetchSteamGamesDTO.getSteamName());
    }
}
