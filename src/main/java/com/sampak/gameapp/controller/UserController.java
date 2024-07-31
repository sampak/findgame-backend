package com.sampak.gameapp.controller;

import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.dto.requests.UserCreateRequestDTO;
import com.sampak.gameapp.dto.requests.UserSignInRequestDTO;
import com.sampak.gameapp.dto.responses.TokenResponseDTO;
import com.sampak.gameapp.dto.responses.UserResponseDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.providers.CurrentUserProvider.CurrentUserProvider;
import com.sampak.gameapp.service.SteamService;
import com.sampak.gameapp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void create(@RequestBody UserCreateRequestDTO user) {
        userService.create(mapToUser(user));

    }

    @GetMapping("/me")
    public UserResponseDTO getMe() {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return mapToUserResponseDTO(user);
    }

    @GetMapping("/games")
    public Set<GamesResponseDTO> getGames() {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return user.getGames().stream()
                .map(game -> new GamesResponseDTO(game.getId(), game.getName()))
                .collect(Collectors.toSet());
    }


    @GetMapping("/steam")
    public void getSteam() {
        System.out.println(steamService.getSteamId("sampak"));
    }
}
