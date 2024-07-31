package com.sampak.gameapp.mapper;

import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.entity.GameEntity;

public class GameMapper {
    public static GamesResponseDTO gameToGamesResponseDTO(GameEntity game) {
        return GamesResponseDTO.builder().id(game.getId()).name(game.getName()).appId(game.getAppId()).build();
    }
}
