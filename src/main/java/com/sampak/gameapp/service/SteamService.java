package com.sampak.gameapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampak.gameapp.dto.LoginProvider;
import com.sampak.gameapp.dto.Test;
import com.sampak.gameapp.dto.responses.GetSteamProfileDetailsDTO;
import com.sampak.gameapp.dto.responses.TokenResponseDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.exception.AppException;
import com.sampak.gameapp.repository.GameRepository;
import com.sampak.gameapp.repository.UserRepository;
import com.sampak.steamLib.SteamAuthenticator;
import lombok.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SteamService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private GameRepository gameRepository;


    private final UserRepository userRepository;
    @Value("${STEAM_API_KEY}")
    private String key;

    @Value("${FRONTEND_URL}")
    private String frontendURL;



    public List<GameEntity> getGames(String steamId) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = String.format("https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=%s&steamid=%s&format=json&include_appinfo=true&include_played_free_games=true", key, steamId);
        String jsonResponse = restTemplate.getForObject(requestUrl, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        List<GameEntity> gamesList = new ArrayList<>();
        try {

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode gamesNode = rootNode.path("response").path("games");

            if(gamesNode.isArray()) {
                for(JsonNode gameNode : gamesNode) {
                    GameEntity game = new GameEntity();
                    game.setAppId(gameNode.path("appid").asText());
                    game.setName(gameNode.path("name").asText());
                    gamesList.add(game);
                }
            }

            return gamesList;
        } catch (Exception e) {
            throw new AppException("Error retriving games", "STEAM_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public GetSteamProfileDetailsDTO getSteamProfileDetails(String steamId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String requestUrl = String.format("https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s", key, steamId);
            String jsonResponse = restTemplate.getForObject(requestUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode playerNode = rootNode.path("response").path("players").get(0);

            String avatarfull = playerNode.path("avatarfull").asText();
            String loccountrycode = playerNode.path("loccountrycode").asText();
            String personaname = playerNode.path("personaname").asText();

            return new GetSteamProfileDetailsDTO(avatarfull, loccountrycode, personaname);
        } catch(Exception e) {
            throw new AppException("Error", "STEAM_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getSteamId(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String requestUrl = String.format("https://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?key=%s&vanityurl=%s", key, url);
            String jsonResponse = restTemplate.getForObject(requestUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode responseNode = rootNode.path("response");
            Test result = objectMapper.treeToValue(responseNode, Test.class);
            return result.getSteamid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException("Error retriving steamId", "STEAM_ERROR", HttpStatus.BAD_REQUEST);
        }
    }

    public String getLoginUrl() {
        SteamAuthenticator steamAuthenticator = new SteamAuthenticator();
        return steamAuthenticator.authenticate(frontendURL + "/verify");
    }

    public void getSteamGamesFromVerify(String steamId, UserEntity user) {
        List<GameEntity> games = getGames(steamId);
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

    public TokenResponseDTO verify(UserEntity user, String fullUrl, Map<String, String> queryParams) {
        SteamAuthenticator steamAuthenticator = new SteamAuthenticator();
        String steamId = steamAuthenticator.verify(fullUrl, queryParams);
        if(steamId == null) {
            throw new AppException("Error", "STEAM_ERROR", HttpStatus.BAD_REQUEST);
        }

        GetSteamProfileDetailsDTO steamProfileDetails = getSteamProfileDetails(steamId);

        if(user == null) {

            Optional<UserEntity> isExist = userRepository.findBySteamId(steamId);

            if (isExist.isPresent()) {
                System.out.println("user founded");
                String token = jwtService.generateToken(String.valueOf(isExist.get().getId()));
                return new TokenResponseDTO(token);
            }
            System.out.println("user not founded");

            UserEntity userEntity = new UserEntity();
            userEntity.setSteamId(steamId);
            userEntity.setLocation(steamProfileDetails.getLoccountrycode());
            userEntity.setLogin(steamProfileDetails.getPersonaname());
            userEntity.setAvatar(steamProfileDetails.getAvatar());
            userEntity.setLoginProvider(LoginProvider.STEAM);
            userRepository.save(userEntity);
            String token = jwtService.generateToken(String.valueOf(userEntity.getId()));
            getSteamGamesFromVerify(steamId, userEntity);
            return new TokenResponseDTO(token);
        }

        user.setSteamId(steamId);
        user.setAvatar(steamProfileDetails.getAvatar());
        user.setLocation(steamProfileDetails.getLoccountrycode());
        getSteamGamesFromVerify(steamId, user);
        return new TokenResponseDTO("");

    }
}
