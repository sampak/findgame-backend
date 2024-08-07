package com.sampak.gameapp.repository;

import com.sampak.gameapp.entity.FriendEntity;
import com.sampak.gameapp.entity.FriendStatus;
import com.sampak.gameapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, UUID> {
    Optional<FriendEntity> findByUserAndFriend(UserEntity user, UserEntity friend);
    Optional<FriendEntity> findById(UUID id);
    List<FriendEntity> findByUserOrFriend(UserEntity user, UserEntity friend);
    List<FriendEntity> findByUserOrFriendAndStatus(UserEntity user, UserEntity friend, FriendStatus status);

}
