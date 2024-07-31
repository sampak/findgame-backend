package com.sampak.gameapp.dto.requests;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcceptUserDTO {
    private String inviteId;
}
