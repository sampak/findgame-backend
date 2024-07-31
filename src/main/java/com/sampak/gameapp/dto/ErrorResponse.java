package com.sampak.gameapp.dto;

import lombok.*;

@Getter
@Setter
@Data
public class ErrorResponse {
    private boolean success;
    private String errorCode;
    private String message;
    private int status;

    public ErrorResponse(String errorCode, String message, int status) {
        this.success = false;
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

}
