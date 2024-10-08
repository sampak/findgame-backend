package com.sampak.gameapp.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.sampak.gameapp.dto.FriendRemove;
import com.sampak.gameapp.dto.FriendSocket;
import com.sampak.gameapp.dto.FriendStatusChange;
import com.sampak.gameapp.dto.requests.AcceptUserDTO;
import com.sampak.gameapp.dto.requests.DeclineOrRemoveUserDTO;
import com.sampak.gameapp.dto.requests.InviteUserDTO;
import com.sampak.gameapp.dto.responses.FriendDTO;
import com.sampak.gameapp.entity.FriendEntity;
import com.sampak.gameapp.entity.FriendStatus;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.providers.CurrentUserProvider.CurrentUserProvider;
import com.sampak.gameapp.service.FriendService;
import com.sampak.gameapp.service.SocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sampak.gameapp.mapper.UserMapper.mapToUserResponseDTO;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    FriendService friendService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    private SocketIOServer server;

    private SocketService socketService;

    private DataListener<Void> getOnlineFriends() {
        return (client, data, ackSender) -> {
            List<UUID> onlineFriends = socketService.getOnlineFriends(UUID.fromString(client.get("userId")));
            List<String> onlineFriendsAsString = onlineFriends.stream()
                    .map(UUID::toString)
                    .collect(Collectors.toList());
            client.sendEvent(FriendSocket.FRIEND_ONLINE_LIST.toString(), onlineFriendsAsString);
            System.out.println("onlineFriends: " + onlineFriendsAsString);
        };
    }

    FriendController(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addEventListener(FriendSocket.FRIEND_ONLINE_LIST.toString(), void.class, this.getOnlineFriends());
    }

    @GetMapping("")
    public List<FriendDTO> getFriends() {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return friendService.getFriends(user);
    }


    @PostMapping("")
    public FriendDTO invite(@RequestBody InviteUserDTO inviteUserDTO)  {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        FriendDTO friendEntity = friendService.invite(user, inviteUserDTO);
        FriendDTO toFriend = new FriendDTO();
        toFriend.setId(friendEntity.getId());
        toFriend.setStatus(friendEntity.getStatus());
        toFriend.setUser(mapToUserResponseDTO(user));
        toFriend.setMyInvitation(false);
        socketService.sendMessage(UUID.fromString(inviteUserDTO.getId()), FriendSocket.FRIEND_INVITATION.toString(), toFriend);
        return friendEntity;
    }

    @PutMapping("/accept")
    public void accept(@RequestBody AcceptUserDTO acceptUserDTO) {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        FriendEntity friendInvitation = friendService.acceptInvite(user, acceptUserDTO);
        FriendStatusChange friendStatusChange  = new FriendStatusChange(friendInvitation.getId(), FriendStatus.FRIENDS);
        socketService.sendMessage(friendInvitation.getUser().getId(), FriendSocket.FRIEND_STATUS_CHANGE.toString(), friendStatusChange);
    }

    @DeleteMapping("")
    public void delete(@RequestBody DeclineOrRemoveUserDTO declineOrRemoveUserDTO) {
        UserEntity  user = currentUserProvider.getCurrentUserEntity();
        UUID friendId = friendService.declineOrRemove(user, declineOrRemoveUserDTO);
        socketService.sendMessage(friendId, FriendSocket.FRIEND_REMOVE.toString(), new FriendRemove(UUID.fromString(declineOrRemoveUserDTO.getInviteId())));
    }
}
