package com.sampak.gameapp.controller;

import com.sampak.gameapp.dto.requests.AcceptUserDTO;
import com.sampak.gameapp.dto.requests.InviteUserDTO;
import com.sampak.gameapp.entity.UserEntity;
import com.sampak.gameapp.providers.CurrentUserProvider.CurrentUserProvider;
import com.sampak.gameapp.service.FriendService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
public class FriendsController {

    @Autowired
    FriendService friendService;

    @Autowired
    private CurrentUserProvider currentUserProvider;


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

}
