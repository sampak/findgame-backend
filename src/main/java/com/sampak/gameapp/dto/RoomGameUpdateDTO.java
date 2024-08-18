package com.sampak.gameapp.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class RoomGameUpdateDTO {
    private String roomId;
    private List<String> games;
}
