package com.sampak.gameapp.dto.requests;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserSignInRequestDTO {
    private String password;
    private String login;
}
