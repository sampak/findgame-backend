package com.sampak.gameapp.dto;

import com.sampak.gameapp.dto.responses.UserResponseDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WheelRoom {
    private UUID id;
    private List<UserResponseDTO> users;
    private List<GameEntity> games;
    private RoomStatus status;
    private List<UserResponseDTO> spectators;
    private UserResponseDTO creator;
}
