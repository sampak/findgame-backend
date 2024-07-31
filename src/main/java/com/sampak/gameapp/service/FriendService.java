package com.sampak.gameapp.service;

import com.sampak.gameapp.dto.requests.AcceptUserDTO;
import com.sampak.gameapp.dto.requests.DeclineOrRemoveUserDTO;
import com.sampak.gameapp.dto.requests.InviteUserDTO;
import com.sampak.gameapp.entity.FriendEntity;
import com.sampak.gameapp.entity.FriendStatus;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.exception.AppException;
import com.sampak.gameapp.repository.FriendRepository;
import com.sampak.gameapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FriendService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    public void invite(UserEntity user, InviteUserDTO inviteUserDTO)  {
        UUID toUserId = UUID.fromString(inviteUserDTO.getId());
        Optional<UserEntity> toUser = userRepository.findById(toUserId);

        if (!toUser.isPresent()) {
            throw new AppException("Friend doesn't exist", "FRIEND_NOT_EXIST", HttpStatus.NOT_FOUND);
        }
        boolean relationshipExists = friendRepository.findByUserAndFriend(user, toUser.get()).isPresent() ||
                friendRepository.findByUserAndFriend(toUser.get(), user).isPresent();


        if (relationshipExists) {
            throw new AppException("Friend Already exist", "FRIEND_EXIST", HttpStatus.BAD_REQUEST);
        }

        FriendEntity friend = FriendEntity.builder().user(user).friend(toUser.get()).status(FriendStatus.INVITED).build();
        friendRepository.save(friend);
    }

    public void acceptInvite(UserEntity user, AcceptUserDTO inviteUserDTO)  {
        UUID invitationId = UUID.fromString(inviteUserDTO.getInviteId());

        Optional<FriendEntity> invitation = friendRepository.findById(invitationId);

        if(!invitation.isPresent()) {
            throw new AppException("Invitation is not exist", "FRIEND_NOT_EXIST", HttpStatus.NOT_FOUND);
        }

        FriendEntity invitationFriend = invitation.get();

        boolean isYourInvitation = invitationFriend.getUser().getId().equals(user.getId());


        if (!invitationFriend.getUser().getId().equals(user.getId()) && !invitationFriend.getFriend().getId().equals(user.getId())) {
            throw new AppException("Invitation is not exist", "FRIEND_NOT_EXIST", HttpStatus.NOT_FOUND);
        }

        if(invitationFriend.getStatus().equals(FriendStatus.FRIENDS) || (invitationFriend.getStatus().equals(FriendStatus.WAITING_FOR_ACCEPT) && isYourInvitation)) {
            throw new AppException("Invitation is not exist", "FRIEND_NOT_EXIST", HttpStatus.NOT_FOUND);
        }

        invitationFriend.setStatus(FriendStatus.FRIENDS);
        friendRepository.save(invitationFriend);
    }

    public void declineOrRemove(UserEntity user, DeclineOrRemoveUserDTO inviteUserDTO)  {
        UUID invitationId = UUID.fromString(inviteUserDTO.getInviteId());

        Optional<FriendEntity> friendRelation = friendRepository.findById(invitationId);

        if(!friendRelation.isPresent()) {
            throw new AppException("Invitation is not exist", "FRIEND_NOT_EXIST", HttpStatus.NOT_FOUND);
        }

        FriendEntity friend = friendRelation.get();


        if (!friend.getUser().getId().equals(user.getId()) && !friend.getFriend().getId().equals(user.getId())) {
            throw new AppException("Invitation is not exist", "FRIEND_NOT_EXIST", HttpStatus.NOT_FOUND);
        }

        friendRepository.delete(friend);
    }
}
