package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.mapper.GameMapper;
import com.sampak.gameapp.repository.GameRepository;
import com.sampak.gameapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class GameService {

    @Autowired
    private SteamService steamService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<GamesResponseDTO> fetchUserGamesFromSteam(UserEntity user, String steamId) {

        List<GameEntity> games = steamService.getGames(steamId);
        System.out.println(games);
        saveGamesToDatabase(games, user);
        user = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getGames().stream()
                .map(GameMapper::gameToGamesResponseDTO)
                .collect(Collectors.toList());
    }

    private void saveGamesToDatabase(List<GameEntity> games, UserEntity user) {

        for (GameEntity game : games) {
            Optional<GameEntity> existingGame = gameRepository.findByAppId(game.getAppId());

            if (existingGame.isPresent()) {
                GameEntity gameEntity = existingGame.get();

                if (user.getGames().contains(gameEntity)) {

                    System.out.println("User already owns the game: " + gameEntity.getName());
                    continue;
                }
                user.getGames().add(gameEntity);
            } else {
                gameRepository.save(game);
                user.getGames().add(game);
            }
        }
        userRepository.save(user);
    }

    @Scheduled(cron = "0 0 23 * * ?")
    @Transactional
    public void performGamesUpdate() {
        List<UserEntity> users = userRepository.findAll();
        for (UserEntity user : users) {
            if(user.getSteamId() == null) {
                continue;
            }
            fetchUserGamesFromSteam(user, user.getSteamId());
        }
    }
}
