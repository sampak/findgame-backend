package com.sampak.gameapp.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserCreateRequestDTO {
    @NotBlank(message = "WRONG_EMAIL")
    @Email(message = "WRONG_EMAIL")
    private String email;
    @NotBlank(message = "WRONG_PASSWORD")
    private String password;
    @NotBlank(message = "WRONG_LOGIN")
    private String login;
}
