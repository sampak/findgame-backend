package com.sampak.gameapp.controller;

import com.sampak.gameapp.dto.requests.ChangeSteamIdDTO;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return userService.create(mapToUser(user));

    }

    @PatchMapping("/steamid")
    public void updateSteamId(@Valid @RequestBody ChangeSteamIdDTO changeSteamIdDTO) {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        userService.updateSteamId(user, changeSteamIdDTO);
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
                .map(game -> new GamesResponseDTO(game.getId(), game.getAppId(), game.getName()))
                .collect(Collectors.toSet());
    }

    @GetMapping("/discovery")
//    public List<UserResponseDTO> getDiscovery() {
    public void getDiscovery() {
//        userService.getAll();
    }

}
