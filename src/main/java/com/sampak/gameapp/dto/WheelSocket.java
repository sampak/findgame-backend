package com.sampak.gameapp.dto;

import com.fasterxml.jackson.annotation.JsonValue;


public enum WheelSocket {
    ROOM_JOIN,
    ROOM_LEAVE,
    ROOM_UPDATE,
    ROOM_INSERT_GAMES,
    ROOM_REMOVE_GAMES,
    ROOM_ROLL;

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
