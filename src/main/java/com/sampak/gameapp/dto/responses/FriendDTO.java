package com.sampak.gameapp.dto.responses;

import com.sampak.gameapp.entity.FriendStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDTO {
    private UUID id;
    private UserResponseDTO user;
    private FriendStatus status;
}
