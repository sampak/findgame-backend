package com.sampak.gameapp.dto.responses;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UpdateSteamIdDTO {
    private String avatar;
    private String location;
}
