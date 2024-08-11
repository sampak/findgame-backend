package com.sampak.gameapp.service.impl;

import com.sampak.gameapp.dto.RoomStatus;
import com.sampak.gameapp.dto.WheelRoom;
import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.dto.requests.RequestWheelDTO;
import com.sampak.gameapp.dto.responses.UserResponseDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.exception.AppException;
import com.sampak.gameapp.mapper.GameMapper;
import com.sampak.gameapp.mapper.UserMapper;
import com.sampak.gameapp.repository.GameRepository;
import com.sampak.gameapp.repository.UserRepository;
import com.sampak.gameapp.service.WheelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sampak.gameapp.mapper.UserMapper.mapToUserResponseDTO;

@Service
public class WheelServiceImpl implements WheelService {


    private final RedisServiceImpl redisService;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public WheelServiceImpl(RedisServiceImpl redisService, GameRepository gameRepository, UserRepository userRepository) {
        this.redisService = redisService;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    // TODO START WHEEL SOCKETS

    private String buildRoomName(String roomId) {
        return "wheelRoom_" + roomId;
    }

    private WheelRoom findRoom(String roomId) {
        String key = buildRoomName(roomId);
        return redisService.get(key, WheelRoom.class);
    }

    public WheelRoom leaveSpectators(UserEntity user, String roomId) {
        WheelRoom room = findRoom(roomId);

        if (room == null) {
//            throw new AppException("ROOM_NOT_FOUND", "ROOM_NOT_FOUND", HttpStatus.NOT_FOUND);
            return null;
        }

        UserResponseDTO userDTO = mapToUserResponseDTO(user);
        List<UserResponseDTO> spectators = room.getSpectators();

        boolean userInSpectators = spectators.stream()
                .anyMatch(spectator -> spectator.getId().equals(userDTO.getId()));

        if (!userInSpectators) {
//            throw new AppException("User is not a spectator in this room", "USER_NOT_IN_SPECTATORS", HttpStatus.BAD_REQUEST);
            return null;
        }

        spectators = spectators.stream()
                .filter(spectator -> !spectator.getId().equals(userDTO.getId()))
                .collect(Collectors.toList());

        room.setSpectators(spectators);

        String key = buildRoomName(roomId);
        redisService.save(key, room);
        return room;
    }

    public WheelRoom joinToSpectators(UserEntity user, String roomId) {
        WheelRoom room = findRoom(roomId);

        if (room == null) {
//            throw new AppException("ROOM_NOT_FOUND", "ROOM_NOT_FOUND", HttpStatus.NOT_FOUND);
            return null;
        }

        UserResponseDTO userDTO = mapToUserResponseDTO(user);
        List<UserResponseDTO> spectators = room.getSpectators();

        boolean userAlreadyInSpectators = spectators.stream()
                .anyMatch(spectator -> spectator.getId().equals(userDTO.getId()));

        if (userAlreadyInSpectators) {
//            throw new AppException("User is already a spectator in this room", "USER_ALREADY_IN_SPECTATORS", HttpStatus.BAD_REQUEST);
            return null;
        }

        spectators.add(userDTO);
        room.setSpectators(spectators);

        String key = buildRoomName(roomId);
        redisService.save(key, room);

        return room;
    }

    public void removeGamesFromRoom(String roomId, List<String> gameIds) {
        WheelRoom room = findRoom(roomId);
        if(room == null) {
            throw new AppException("ROOM_NOT_FOUND", "ROOM_NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        // TODO

    }

    public void insertGamesToRoom(String roomId, List<String> gameIds) {
        WheelRoom room = findRoom(roomId);
        if(room == null) {
            throw new AppException("ROOM_NOT_FOUND", "ROOM_NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        List<GameEntity> games = gameRepository.findAllByIdIn(gameIds.stream().map(id -> UUID.fromString(id)).collect(Collectors.toList()));
        games.forEach(game -> {
            game.setUsers(null);
        });

        List<GameEntity> actualGames = room.getGames();
        actualGames.addAll(games);
        String key = buildRoomName(roomId);
        redisService.save(key, room);
    }

    public List<GamesResponseDTO> getSharedGames(String roomId) {
        WheelRoom room = findRoom(roomId);

        if(room == null) {
            throw new AppException("ROOM_NOT_FOUND", "ROOM_NOT_FOUND", HttpStatus.NOT_FOUND);
        }


        Set<Set<GameEntity>> usersGames = new HashSet<>();

        for (UserResponseDTO roomUser : room.getUsers()) {
            UserEntity user = userRepository.findById(UUID.fromString(roomUser.getId())).orElseThrow(() -> new IllegalArgumentException("User not found"));
            usersGames.add(user.getGames());
        }

        Set<GameEntity> commonGames = usersGames.stream()
                .skip(1)
                .collect(() -> new HashSet<>(usersGames.iterator().next()), Set::retainAll, Set::retainAll);

        return commonGames.stream().map(GameMapper::gameToGamesResponseDTO).sorted(Comparator.comparing(GamesResponseDTO::getName)).collect(Collectors.toList());
    }


    public WheelRoom getRoom(String roomId) {
        WheelRoom room = findRoom(roomId);

        if(room == null) {
            throw new AppException("ROOM_NOT_FOUND", "ROOM_NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        return room;
    }




    public WheelRoom createRoom(UserEntity userEntity, RequestWheelDTO requestWheelDTO) {
        UUID roomId = UUID.randomUUID();
        String key = "wheelRoom_" + roomId.toString();
        List<UserResponseDTO> invitedUsers = userRepository.findAllByIdIn(requestWheelDTO.getUsers()).stream().map(UserMapper::mapToUserResponseDTO).collect(Collectors.toList());
        UserResponseDTO me = mapToUserResponseDTO(userEntity);
        invitedUsers.add(me);
        redisService.saveWithTTL(key, new WheelRoom(roomId, invitedUsers, List.of(), RoomStatus.WAITING, List.of(), me), 1, TimeUnit.HOURS);
        return redisService.get(key, WheelRoom.class);
    }
}