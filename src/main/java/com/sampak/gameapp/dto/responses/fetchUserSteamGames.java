package com.sampak.gameapp.dto.responses;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class fetchUserSteamGames {
    private String steamId;
}
