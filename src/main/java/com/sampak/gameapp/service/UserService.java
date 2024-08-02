package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.ChangeSteamIdDTO;
import com.sampak.gameapp.dto.requests.UserSignInRequestDTO;
import com.sampak.gameapp.dto.responses.TokenResponseDTO;
import com.sampak.gameapp.dto.responses.UserResponseDTO;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.exception.AppException;
import com.sampak.gameapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void getAll() {
//        return userRepository.findAll();

    }

    public TokenResponseDTO login(UserSignInRequestDTO user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
         UserEntity userEntity = userRepository.findByEmailOrLogin(user.getLogin(), user.getLogin()).orElseThrow();
         String token = jwtService.generateToken(String.valueOf(userEntity.getId()));

         return new TokenResponseDTO(token);

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

    public void updateSteamId(UserEntity user, ChangeSteamIdDTO changeSteamIdDTO) {
        user.setSteamId(changeSteamIdDTO.getSteamId());
        userRepository.save(user);
    }
}
