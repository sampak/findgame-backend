package com.sampak.gameapp.dto;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WheelGame {
    private UUID id;
    private String appId;
    private String name;
    private Boolean isWinned;
}
