package com.sampak.gameapp.dto.requests;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestVerifyDTO {
    private String requestURL;
    private Map<String, String> queryParams;
}
