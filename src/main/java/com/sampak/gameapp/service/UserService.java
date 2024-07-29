package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.UserSignInRequestDTO;
import com.sampak.gameapp.dto.responses.TokenResponseDTO;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public TokenResponseDTO login(UserSignInRequestDTO user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
         UserEntity userEntity = userRepository.findByEmailOrLogin(user.getLogin(), user.getLogin()).orElseThrow();
         String token = jwtService.generateToken(String.valueOf(userEntity.getId()));

         return new TokenResponseDTO(token);

    }

    public void create(UserEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
