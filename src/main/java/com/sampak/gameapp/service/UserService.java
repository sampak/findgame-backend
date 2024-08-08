package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.ChangeSteamIdDTO;
import com.sampak.gameapp.dto.requests.UserSignInRequestDTO;
import com.sampak.gameapp.dto.responses.DiscoveryUserDTO;
import com.sampak.gameapp.dto.responses.GetSteamProfileDetailsDTO;
import com.sampak.gameapp.dto.responses.TokenResponseDTO;
import com.sampak.gameapp.dto.responses.UpdateSteamIdDTO;
import com.sampak.gameapp.entity.GameEntity;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.exception.AppException;
import com.sampak.gameapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.sampak.gameapp.mapper.UserMapper.mapToDiscoveryUserDTO;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private SteamService steamService;

    public List<DiscoveryUserDTO> getAll(UserEntity user) {
        try {
            List<UserEntity> users = userRepository.findAll();
            List<DiscoveryUserDTO> discoveryUsers = new ArrayList<>();
            for (UserEntity userEntity : users) {
                if (userEntity.getId().equals(user.getId())) {
                    continue;
                }
            double percent = getCommonGamesPercentage(user.getId(), userEntity.getId());
                discoveryUsers.add(mapToDiscoveryUserDTO(userEntity, percent));
            }

            return discoveryUsers;
        } catch(Exception e) {
            throw new AppException("Calculate score error", "CALCULATE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TokenResponseDTO login(UserSignInRequestDTO user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
             Optional<UserEntity> userEntity = userRepository.findByEmailOrLogin(user.getLogin(), user.getLogin());
             if(!userEntity.isPresent()) {
                 throw new AppException("NOT_FOUND", "NOT_FOUND", HttpStatus.NOT_FOUND);
             }
             String token = jwtService.generateToken(String.valueOf(userEntity.get().getId()));

             return new TokenResponseDTO(token);
        } catch (Exception e) {
            throw new AppException("NOT_FOUND", "NOT_FOUND", HttpStatus.NOT_FOUND);
        }

    }

    public TokenResponseDTO create(UserEntity user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            String token = jwtService.generateToken(String.valueOf(user.getId()));
            return new TokenResponseDTO(token);
        } catch(Exception ex) {
            throw new AppException("User is exist", "USER_EXIST", HttpStatus.BAD_REQUEST);
        }

    }

    public UpdateSteamIdDTO updateSteamId(UserEntity user, ChangeSteamIdDTO changeSteamIdDTO) {
        try {
            GetSteamProfileDetailsDTO steamProfileDetailsDTO = steamService.getSteamProfileDetails(changeSteamIdDTO.getSteamId());
            user.setAvatar(steamProfileDetailsDTO.getAvatar());
            user.setLocation(steamProfileDetailsDTO.getLoccountrycode());
            user.setSteamId(changeSteamIdDTO.getSteamId());
            userRepository.save(user);

            return new UpdateSteamIdDTO(steamProfileDetailsDTO.getAvatar(), steamProfileDetailsDTO.getLoccountrycode());
        } catch (Exception e) {
            throw new AppException("Update profile failed", "UPDATED_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public double getCommonGamesPercentage(UUID... userIds) {
        if (userIds.length < 2) {
            throw new IllegalArgumentException("Must provide at least two user IDs");
        }

        // Pobierz zestawy gier wszystkich użytkowników
        Set<Set<GameEntity>> usersGames = new HashSet<>();
        for (UUID userId : userIds) {
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
            usersGames.add(user.getGames());
        }

        // Znajdź wspólne gry
        Set<GameEntity> commonGames = usersGames.stream()
                .skip(1)
                .collect(() -> new HashSet<>(usersGames.iterator().next()), Set::retainAll, Set::retainAll);

        // Oblicz procent wspólnych gier
        long totalGames = usersGames.stream().flatMap(Set::stream).distinct().count();

        if(totalGames == 0) {
            return 0;
        }

        double commonGamesPercentage = (double) commonGames.size() / totalGames * 100;
        return commonGamesPercentage;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void performUpdateProfile() {
        List<UserEntity> users = userRepository.findAll();

        for(UserEntity user : users) {
            if(user.getSteamId() == null) {
                continue;
            }

            LocalDateTime lastProfileUpdate = user.getLastProfileUpdate();
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

            if(lastProfileUpdate != null && !lastProfileUpdate.isBefore(oneHourAgo)) {
                continue;
            }

            GetSteamProfileDetailsDTO steamProfile = steamService.getSteamProfileDetails(user.getSteamId());

            user.setAvatar(steamProfile.getAvatar());
            user.setLocation(steamProfile.getLoccountrycode());
            user.setLastProfileUpdate(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
