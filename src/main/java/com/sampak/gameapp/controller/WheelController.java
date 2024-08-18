package com.sampak.gameapp.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.sampak.gameapp.dto.RoomGameUpdateDTO;
import com.sampak.gameapp.dto.WheelGame;
import com.sampak.gameapp.dto.WheelRoom;
import com.sampak.gameapp.dto.WheelSocket;
import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.dto.requests.RequestWheelDTO;
import com.sampak.gameapp.dto.responses.UserResponseDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.providers.CurrentUserProvider.CurrentUserProvider;
import com.sampak.gameapp.repository.UserRepository;
import com.sampak.gameapp.service.SocketService;
import com.sampak.gameapp.service.WheelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/wheel")
public class WheelController {

    private final WheelService wheelService;
    private final CurrentUserProvider currentUserProvider;
    private SocketIOServer server;
    private UserRepository  userRepository;
    private SocketService socketService;


    public WheelController(WheelService wheelService, CurrentUserProvider currentUserProvider, SocketIOServer server, UserRepository userRepository, SocketService socketService) {
        this.wheelService = wheelService;
        this.currentUserProvider = currentUserProvider;
        this.server = server;
        this.userRepository = userRepository;
        this.socketService = socketService;
        server.addEventListener(WheelSocket.ROOM_JOIN.toString(), String.class, this.joinToSpectators());
        server.addEventListener(WheelSocket.ROOM_LEAVE.toString(), String.class, this.leaveFromSpectators());
        server.addEventListener(WheelSocket.ROOM_INSERT_GAMES.toString(), RoomGameUpdateDTO.class, this.insertGames());
        server.addEventListener(WheelSocket.ROOM_REMOVE_GAMES.toString(), RoomGameUpdateDTO.class, this.removeGames());
        server.addEventListener(WheelSocket.ROOM_ROLL.toString(), String.class, this.rollGame());
    }

    private DataListener<String> rollGame() {
        return (client, data, ackSender) -> {
            String roomId = data;
            List<WheelGame> games = wheelService.roll(roomId);

            if(games == null) {
                return;
            }

            WheelRoom room = wheelService.getRoom(roomId);

            for (UserResponseDTO user : room.getSpectators()) {
                socketService.sendMessage(UUID.fromString(user.getId()), WheelSocket.ROOM_ROLL.toString(), games);
            }

        };
    }

    private DataListener<RoomGameUpdateDTO> removeGames() {
        return (client, data, ackSender) -> {
            UUID userId = UUID.fromString(client.get("userId"));
            String roomId = data.getRoomId();
            List<String> gameIds = data.getGames();

            Optional<UserEntity> userEntity = userRepository.findById(userId);
            if(!userEntity.isPresent()) {
                return;
            }

            wheelService.removeGamesFromRoom(roomId, gameIds);
            WheelRoom room = wheelService.getRoom(roomId);

            for (UserResponseDTO user : room.getSpectators()) {
                socketService.sendMessage(UUID.fromString(user.getId()), WheelSocket.ROOM_UPDATE.toString(), room);
            }
        };
    }

    private DataListener<RoomGameUpdateDTO> insertGames() {
        return (client, data, ackSender) -> {
            System.out.println("Received data: " + data);
            UUID userId = UUID.fromString(client.get("userId"));
            String roomId = data.getRoomId();
            List<String> gameIds = data.getGames();

            Optional<UserEntity> userEntity = userRepository.findById(userId);
            if(!userEntity.isPresent()) {
                return;
            }

            wheelService.insertGamesToRoom(roomId, gameIds);
            WheelRoom room = wheelService.getRoom(roomId);

            for (UserResponseDTO user : room.getSpectators()) {
                socketService.sendMessage(UUID.fromString(user.getId()), WheelSocket.ROOM_UPDATE.toString(), room);
            }
        };
    }


    private DataListener<String> joinToSpectators() {
        return (client, data, ackSender) -> {
            UUID userId = UUID.fromString(client.get("userId"));
            System.out.println("Wrzucam gracza: " + data + userId);
            String roomId = data.toString();

            Optional<UserEntity> userEntity = userRepository.findById(userId);
            if(!userEntity.isPresent()) {
                return;
            }

            wheelService.joinToSpectators(userEntity.get(), roomId);
            WheelRoom room = wheelService.getRoom(roomId);

            for (UserResponseDTO user : room.getSpectators()) {
                socketService.sendMessage(UUID.fromString(user.getId()), WheelSocket.ROOM_UPDATE.toString(), room);
            }
        };
    }

    private DataListener<String> leaveFromSpectators() {
        return (client, data, ackSender) -> {

            System.out.println("Leaving: " + data);
            UUID userId = UUID.fromString(client.get("userId"));
            String roomId = data.toString();

            Optional<UserEntity> userEntity = userRepository.findById(userId);
            if(!userEntity.isPresent()) {
                return;
            }

            wheelService.leaveSpectators(userEntity.get(), roomId);
            WheelRoom room = wheelService.getRoom(roomId);

            System.out.println(room.getSpectators());

            for (UserResponseDTO user : room.getSpectators()) {
                socketService.sendMessage(UUID.fromString(user.getId()), WheelSocket.ROOM_UPDATE.toString(), room);
            }
        };
    }

    @GetMapping("/{roomId}/games")
    public List<GamesResponseDTO> getSharedGames(@PathVariable("roomId") String roomId) {
        return wheelService.getSharedGames(roomId);
    }

    @GetMapping("/{roomId}")
    public WheelRoom getWheel(@PathVariable String roomId) {
        return wheelService.getRoom(roomId);
    }

    @PostMapping("")
    public WheelRoom createWheel(@RequestBody RequestWheelDTO requestWheelDTO) {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return wheelService.createRoom(user, requestWheelDTO);
    }
}
