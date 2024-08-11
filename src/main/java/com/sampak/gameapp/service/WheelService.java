package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.WheelRoom;
import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.dto.requests.RequestWheelDTO;
import com.sampak.gameapp.entity.UserEntity;

import java.util.List;


public interface WheelService {
    WheelRoom getRoom(String roomId);
    WheelRoom joinToSpectators(UserEntity user, String roomId);
    WheelRoom leaveSpectators(UserEntity user, String roomId);
    WheelRoom createRoom(UserEntity userEntity, RequestWheelDTO requestWheelDTO);
    List<GamesResponseDTO> getSharedGames(String roomId);
    void insertGamesToRoom(String roomId, List<String> gameIds);
    void removeGamesFromRoom(String roomId, List<String> gameIds);
}
