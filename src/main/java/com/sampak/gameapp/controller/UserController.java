package com.sampak.gameapp.controller;

import com.sampak.gameapp.dto.LoginProvider;
import com.sampak.gameapp.dto.requests.ChangeSteamIdDTO;
import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.dto.requests.UserCreateRequestDTO;
import com.sampak.gameapp.dto.requests.UserSignInRequestDTO;
import com.sampak.gameapp.dto.responses.*;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.providers.CurrentUserProvider.CurrentUserProvider;
import com.sampak.gameapp.service.SteamService;
import com.sampak.gameapp.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import static com.sampak.gameapp.mapper.UserMapper.mapToUser;
import static com.sampak.gameapp.mapper.UserMapper.mapToUserResponseDTO;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SteamService steamService;

    @Autowired
    private CurrentUserProvider currentUserProvider;


    @GetMapping("")
    public String index() {
        return "hello world";
    }


    @PostMapping("/signin")
    public TokenResponseDTO signin(@RequestBody UserSignInRequestDTO user) {
        return userService.login(user);
    }

    @PostMapping("")
    public TokenResponseDTO create(@Valid @RequestBody UserCreateRequestDTO user) {
        UserEntity newUser  = mapToUser(user);
        newUser.setLoginProvider(LoginProvider.LOGIN);
        return userService.create(newUser);
    }

    @GetMapping("/steamid")
    public ResponseSteamIdDTO getSteamId() {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return new ResponseSteamIdDTO(user.getSteamId());
    }

    @PatchMapping("/steamid")
    public UpdateSteamIdDTO updateSteamId(@Valid @RequestBody ChangeSteamIdDTO changeSteamIdDTO) {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return userService.updateSteamId(user, changeSteamIdDTO);
    }

    @GetMapping("/me")
    public UserResponseDTO getMe() {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return mapToUserResponseDTO(user);
    }

    @GetMapping("/games")
    public List<GamesResponseDTO> getGames() {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return user.getGames().stream()
                .sorted(Comparator.comparing(GameEntity::getName))
                .map(game -> new GamesResponseDTO(game.getId(), game.getAppId(), game.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/discovery")
    public List<DiscoveryUserDTO>  getDiscovery() {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return userService.getAll(user);
    }

}
