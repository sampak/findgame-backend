package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.entity.UserEntity;

import java.util.List;

public interface GameService {
    List<GamesResponseDTO> fetchUserGamesFromSteam(UserEntity user, String steamId);

}
