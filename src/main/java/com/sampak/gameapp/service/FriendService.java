package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.AcceptUserDTO;
import com.sampak.gameapp.dto.requests.DeclineOrRemoveUserDTO;
import com.sampak.gameapp.dto.requests.InviteUserDTO;
import com.sampak.gameapp.dto.responses.FriendDTO;
import com.sampak.gameapp.entity.FriendEntity;
import com.sampak.gameapp.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface FriendService {
    List<FriendDTO> getFriends(UserEntity user);
    FriendDTO invite(UserEntity user, InviteUserDTO inviteUserDTO);
    FriendEntity acceptInvite(UserEntity user, AcceptUserDTO inviteUserDTO);
    UUID declineOrRemove(UserEntity user, DeclineOrRemoveUserDTO inviteUserDTO);
}
