package com.sampak.gameapp.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InviteUserDTO {
    @NotNull
    private String id;
}
