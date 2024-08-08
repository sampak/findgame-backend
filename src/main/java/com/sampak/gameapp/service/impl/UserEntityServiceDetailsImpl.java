package com.sampak.gameapp.service.impl;


import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserEntityServiceDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;


    public UserEntity loadUserById(String id) throws UsernameNotFoundException {
        return userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailOrLogin(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or login: " + username));
    }
}
