package com.sampak.gameapp.repository;

import com.sampak.gameapp.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {
    Optional<GameEntity> findByAppId(String appId);
    List<GameEntity> findAllByIdIn(List<UUID> ids);
}
