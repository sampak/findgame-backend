package com.sampak.gameapp.providers.CurrentUserProvider;

import com.sampak.gameapp.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface CurrentUserProvider {
    String getCurrentUsername();
    UserDetails getCurrentUserDetails();
    UserEntity getCurrentUserEntity();
}
