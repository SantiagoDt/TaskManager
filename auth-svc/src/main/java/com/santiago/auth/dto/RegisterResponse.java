package com.santiago.auth.dto;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    private String message;      // "Registration successful"
    private String username;
    private String email;
    private Instant createdAt;
}
