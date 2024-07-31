package com.sampak.gameapp.dto.requests;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GamesResponseDTO {
    private UUID id;
    private String appId;
    private String name;
}
