package com.sampak.gameapp.mapper;

import com.sampak.gameapp.dto.responses.FriendDTO;
import com.sampak.gameapp.entity.FriendEntity;
import com.sampak.gameapp.entity.UserEntity;

import static com.sampak.gameapp.mapper.UserMapper.mapToUserResponseDTO;

public class FriendMapper {
    public static FriendDTO friendToFriendDTO(FriendEntity friendEntity, UserEntity userEntity) {
        return FriendDTO.builder()
                .id(friendEntity.getId())
                .user(mapToUserResponseDTO(userEntity))
                .status(friendEntity.getStatus())
                .build();
    }
}
