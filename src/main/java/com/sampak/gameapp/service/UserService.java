package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.ChangeSteamIdDTO;
import com.sampak.gameapp.dto.requests.UserSignInRequestDTO;
import com.sampak.gameapp.dto.responses.DiscoveryUserDTO;
import com.sampak.gameapp.dto.responses.TokenResponseDTO;
import com.sampak.gameapp.dto.responses.UpdateSteamIdDTO;
import com.sampak.gameapp.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<DiscoveryUserDTO> getAll(UserEntity userEntity);
    TokenResponseDTO login(UserSignInRequestDTO userSignInRequestDTO);
    TokenResponseDTO create(UserEntity userEntity);
    UpdateSteamIdDTO updateSteamId(UserEntity user, ChangeSteamIdDTO changeSteamIdDTO);
    double getCommonGamesPercentage(UUID... userIds);

}
