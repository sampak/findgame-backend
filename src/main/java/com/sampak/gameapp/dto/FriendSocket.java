package com.sampak.gameapp.dto;

import com.fasterxml.jackson.annotation.JsonValue;


public enum FriendSocket {
    FRIEND_INVITATION,
    FRIEND_STATUS_CHANGE,
    FRIEND_REMOVE;

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
