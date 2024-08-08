package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.AcceptUserDTO;
import com.sampak.gameapp.dto.requests.DeclineOrRemoveUserDTO;
import com.sampak.gameapp.dto.requests.InviteUserDTO;
import com.sampak.gameapp.dto.responses.FriendDTO;
import com.sampak.gameapp.entity.UserEntity;

import java.util.List;

public interface FriendService {
    List<FriendDTO> getFriends(UserEntity user);
    void invite(UserEntity user, InviteUserDTO inviteUserDTO);
    void acceptInvite(UserEntity user, AcceptUserDTO inviteUserDTO);
    void declineOrRemove(UserEntity user, DeclineOrRemoveUserDTO inviteUserDTO);
}
