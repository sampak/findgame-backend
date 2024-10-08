package com.sampak.gameapp.service.impl;

import com.sampak.gameapp.dto.requests.AcceptUserDTO;
import com.sampak.gameapp.dto.requests.DeclineOrRemoveUserDTO;
import com.sampak.gameapp.dto.requests.InviteUserDTO;
import com.sampak.gameapp.dto.responses.FriendDTO;
import com.sampak.gameapp.entity.FriendEntity;
import com.sampak.gameapp.entity.FriendStatus;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.exception.AppException;
import com.sampak.gameapp.repository.FriendRepository;
import com.sampak.gameapp.repository.UserRepository;
import com.sampak.gameapp.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sampak.gameapp.mapper.FriendMapper.friendToFriendDTO;

@Service
public class FriendServiceImpl implements FriendService {

    private final UserRepository userRepository;

    private final FriendRepository friendRepository;

    public FriendServiceImpl(UserRepository userRepository, FriendRepository friendRepository) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
    }

    public List<FriendDTO> getFriends(UserEntity user) {
        List<FriendEntity> friends = friendRepository.findByUserOrFriend(user, user);
        return friends.stream().map(friend -> {
            boolean myInvitation = friend.getUser().getId().equals(user.getId());
            return friendToFriendDTO(friend, myInvitation ? friend.getFriend() : friend.getUser(), myInvitation);
        }).collect(Collectors.toList());
    }

    public FriendDTO invite(UserEntity user, InviteUserDTO inviteUserDTO)  {
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
        return friendToFriendDTO(friend, toUser.get(), true);
    }

    public FriendEntity acceptInvite(UserEntity user, AcceptUserDTO inviteUserDTO)  {
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
        return invitationFriend;
    }

    public UUID declineOrRemove(UserEntity user, DeclineOrRemoveUserDTO inviteUserDTO)  {
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

        return friend.getFriend().getId();
    }
}
