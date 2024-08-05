package com.sampak.gameapp.dto.responses;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscoveryUserDTO {
    private UUID id;
    private String login;
    private String avatar;
    private String location;
    private Double score;
}
