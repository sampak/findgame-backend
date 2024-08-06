package com.sampak.gameapp.providers.CurrentUserProvider;

import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUserProviderImpl implements CurrentUserProvider {

    private final UserRepository userRepository;

    public CurrentUserProviderImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @Override
    public UserDetails getCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }
        throw new IllegalStateException("Current user details are not available.");
    }

    @Override
    public UserEntity getCurrentUserEntity() {
        String id = getCurrentUsername();
        if(id.equals("anonymousUser")) {
            return null;
        }
        return userRepository.getById(UUID.fromString(id));
    }
}
