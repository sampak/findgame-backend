package com.sampak.gameapp.dto.requests;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserCreateRequestDTO {
    private String email;
    private String password;
    private String login;
}
