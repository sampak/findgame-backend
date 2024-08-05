package com.sampak.gameapp.controller;

import com.sampak.gameapp.dto.responses.GetSteamIdDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.providers.CurrentUserProvider.CurrentUserProvider;
import com.sampak.gameapp.service.SteamService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/steam")
public class SteamController {

    @Autowired
    private SteamService steamService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    @GetMapping("/login")
    public String login() {
        return steamService.getLoginUrl();
    }

    @GetMapping("/verify")
    public void verify(HttpServletRequest request,  @RequestParam Map<String, String> queryParams) {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        steamService.verify(user, request.getRequestURL().toString(), queryParams);
    }

    @GetMapping("games/{steamId}")
    public List<GameEntity> getGames(@PathVariable String steamId) {
        return steamService.getGames(steamId);
    }

    @GetMapping("/steamid/{url}")
    public GetSteamIdDTO getSteamId(@PathVariable String url) {
        return new GetSteamIdDTO(steamService.getSteamId(url));
    }
}
