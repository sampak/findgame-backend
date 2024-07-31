package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.GamesResponseDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.mapper.GameMapper;
import com.sampak.gameapp.repository.GameRepository;
import com.sampak.gameapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sampak.gameapp.mapper.GameMapper.gameToGamesResponseDTO;

@Service
public class GameService {

    @Autowired
    private SteamService steamService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<GamesResponseDTO> fetchUserGamesFromSteam(UserEntity user, String steamName) {
        String steamId = steamService.getSteamId(steamName);
        List<GameEntity> games = steamService.getGames(steamId);
        saveGamesToDatabase(games, user);
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
}
