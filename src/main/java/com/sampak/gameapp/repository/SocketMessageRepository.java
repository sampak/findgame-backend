package com.sampak.gameapp.repository;

import java.util.List;
import java.util.UUID;

import com.sampak.gameapp.dto.SocketMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SocketMessageRepository extends JpaRepository<SocketMessage, UUID> {
    List<SocketMessage> findAllByRoom(String room);
}