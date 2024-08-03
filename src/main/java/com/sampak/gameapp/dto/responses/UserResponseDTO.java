package com.sampak.gameapp.dto.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    String id;
    String login;
    String email;
    String steamId;
    String avatar;
    String location;
}
