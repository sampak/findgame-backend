package com.sampak.gameapp.repository;

import com.sampak.gameapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    public UserEntity getById(UUID id);
    public Optional<UserEntity> findByLogin(String username);
    public Optional<UserEntity> findByEmailOrLogin(String email, String login);
    public Optional<UserEntity> findBySteamId(String steamId);
    List<UserEntity> findAllByIdIn(List<UUID> ids);
}
