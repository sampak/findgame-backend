package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.responses.GetSteamProfileDetailsDTO;
import com.sampak.gameapp.dto.responses.TokenResponseDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface SteamService {
    List<GameEntity> getGames(String steamId);
    GetSteamProfileDetailsDTO getSteamProfileDetails(String steamId);
    String getSteamId(String url);
    String getLoginUrl();
    void getSteamGamesFromVerify(String steamId, UserEntity user);
    TokenResponseDTO verify(UserEntity user, String fullUrl, Map<String, String> queryParams);
}
