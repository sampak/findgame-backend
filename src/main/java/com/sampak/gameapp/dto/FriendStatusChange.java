package com.sampak.gameapp.dto;

import com.sampak.gameapp.entity.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendStatusChange {
    private UUID id;
    private FriendStatus status;
}
