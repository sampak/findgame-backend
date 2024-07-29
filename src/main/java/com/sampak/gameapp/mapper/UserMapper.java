package com.sampak.gameapp.mapper;

import com.sampak.gameapp.dto.requests.UserCreateRequestDTO;
import com.sampak.gameapp.entity.UserEntity;

public class UserMapper {


    public static UserEntity mapToUser(UserCreateRequestDTO user) {
        return UserEntity.builder().email(user.getEmail()).login(user.getLogin()).password(user.getPassword()).build();
    }
}
