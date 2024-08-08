package com.sampak.gameapp.controller;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.sampak.gameapp.dto.requests.AcceptUserDTO;
import com.sampak.gameapp.dto.requests.DeclineOrRemoveUserDTO;
import com.sampak.gameapp.dto.requests.InviteUserDTO;
import com.sampak.gameapp.dto.responses.FriendDTO;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.providers.CurrentUserProvider.CurrentUserProvider;
import com.sampak.gameapp.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    FriendService friendService;

    @Autowired
    private CurrentUserProvider currentUserProvider;

    private SocketIOServer server;

    private DataListener<String> test() {
        return (senderClient, data, ackSender) -> {
            System.out.println(Optional.ofNullable(senderClient.get("userId")));
            System.out.println(data);
        };
    }

    FriendController(SocketIOServer server) {
        this.server = server;
        server.addEventListener("send_message", String.class, this.test());
    }

    @GetMapping("")
    public List<FriendDTO> getFriends() {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        return friendService.getFriends(user);
    }


    @PostMapping("")
    public void invite(@RequestBody InviteUserDTO inviteUserDTO)  {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        friendService.invite(user, inviteUserDTO);
    }

    @PutMapping("/accept")
    public void accept(@RequestBody AcceptUserDTO acceptUserDTO) {
        UserEntity user = currentUserProvider.getCurrentUserEntity();
        friendService.acceptInvite(user, acceptUserDTO);
    }

    @DeleteMapping("")
    public void delete(@RequestBody DeclineOrRemoveUserDTO declineOrRemoveUserDTO) {
        UserEntity  user = currentUserProvider.getCurrentUserEntity();
        friendService.declineOrRemove(user, declineOrRemoveUserDTO);
    }




}
