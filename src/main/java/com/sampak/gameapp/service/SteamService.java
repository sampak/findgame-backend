package com.sampak.gameapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sampak.gameapp.dto.Test;
import com.sampak.gameapp.dto.responses.GetSteamProfileDetailsDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.exception.AppException;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SteamService {


    @Value("${STEAM_API_KEY}")
    private String key;



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

            return new GetSteamProfileDetailsDTO(avatarfull, loccountrycode);
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
}
