package com.sampak.gameapp.dto.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetSteamProfileDetailsDTO {
    private String avatar;
    private String loccountrycode;
}
