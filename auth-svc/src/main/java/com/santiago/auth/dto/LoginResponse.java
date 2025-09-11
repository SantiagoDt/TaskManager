package com.santiago.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginResponse {

    private String message;
    private String username;
    private String email;
    private Instant lastLoginAt;
    private String token;


}
