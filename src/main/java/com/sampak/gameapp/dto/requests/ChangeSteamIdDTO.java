package com.sampak.gameapp.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeSteamIdDTO {
    @NotBlank(message = "INVALID_STEAMID")
    public String steamId;
}
