package com.santiago.auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegisterDTO {
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3 , max = 20, message = "Username must be between 3 and 20 characters")
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 10, message = "Password must be at least 10 characters long")
    private String password;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    private String email;
}
