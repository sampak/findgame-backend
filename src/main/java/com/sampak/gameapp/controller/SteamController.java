package com.sampak.gameapp.controller;

import com.sampak.gameapp.dto.requests.RequestVerifyDTO;
import com.sampak.gameapp.dto.responses.GetSteamIdDTO;
import com.sampak.gameapp.dto.responses.TokenResponseDTO;
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

    @PostMapping("/verify")
    public TokenResponseDTO verify(@RequestBody RequestVerifyDTO requestVerifyDTO) {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
//        steamService.verify(user, request.getRequestURL().toString(), queryParams);
        return steamService.verify(user, requestVerifyDTO.getRequestURL(), requestVerifyDTO.getQueryParams());
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
