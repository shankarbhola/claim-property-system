package com.app.claims.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({ "type", "token" })
public class AuthResponse {
    private String type="Bearer";
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}
