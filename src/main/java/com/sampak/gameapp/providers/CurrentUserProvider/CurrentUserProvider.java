package com.sampak.gameapp.providers.CurrentUserProvider;

import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.repository.UserRepository;
import com.sampak.gameapp.service.UserEntityServiceDetails;
import org.springframework.security.core.userdetails.UserDetails;

public interface CurrentUserProvider {
    String getCurrentUsername();
    UserDetails getCurrentUserDetails();
    UserEntity getCurrentUserEntity();
}
