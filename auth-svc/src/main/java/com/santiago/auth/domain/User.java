package com.santiago.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class User {

    @Id
    private String id;
    private String username;
    private String passwordHash;
    private String email;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
    private Role role; // e.g., "USER", "ADMIN"
    private boolean isEmailVerified;
    private Instant lastLoginAt;
    private int failedLoginAttempts;
    private Instant accountLockedUntil;

    public enum Role {
        USER,
        ADMIN
    }

}
