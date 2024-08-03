package com.sampak.gameapp.mapper;

import com.sampak.gameapp.dto.requests.UserCreateRequestDTO;
import com.sampak.gameapp.dto.responses.DiscoveryUserDTO;
import com.sampak.gameapp.dto.responses.UserResponseDTO;
import com.sampak.gameapp.entity.UserEntity;

public class UserMapper {


    public static UserResponseDTO mapToUserResponseDTO(UserEntity userEntity) {
        return UserResponseDTO.builder()
                .id(userEntity.getId().toString())
                .email(userEntity.getEmail())
                .steamId(userEntity.getSteamId())
                .login(userEntity.getLogin())
                .avatar(userEntity.getAvatar())
                .location(userEntity.getLocation())
                .build();
    }

    public static UserEntity mapToUser(UserCreateRequestDTO user) {
        return UserEntity.builder().email(user.getEmail()).login(user.getLogin()).password(user.getPassword()).build();
    }

    public static DiscoveryUserDTO mapToDiscoveryUserDTO(UserEntity user, double percent) {
        return DiscoveryUserDTO.builder().id(user.getId()).login(user.getLogin()).score(percent).location(user.getLocation()).avatar(user.getAvatar()).build();
    }
}
