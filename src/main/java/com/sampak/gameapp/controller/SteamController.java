package com.sampak.gameapp.controller;

import com.sampak.gameapp.dto.responses.GetSteamIdDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.service.SteamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/steam")
public class SteamController {

    @Autowired
    private SteamService steamService;

    @GetMapping("games/{steamId}")
    public List<GameEntity> getGames(@PathVariable String steamId) {
        return steamService.getGames(steamId);
    }

    @GetMapping("/steamid/{url}")
    public GetSteamIdDTO getSteamId(@PathVariable String url) {
        return new GetSteamIdDTO(steamService.getSteamId(url));
    }
}
