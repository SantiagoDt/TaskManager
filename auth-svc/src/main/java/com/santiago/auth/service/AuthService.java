package com.santiago.auth.service;

import com.santiago.auth.domain.User;
import com.santiago.auth.dto.LoginDTO;
import com.santiago.auth.dto.LoginResponse;
import com.santiago.auth.dto.RegisterDTO;
import com.santiago.auth.dto.RegisterResponse;
import com.santiago.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;


    public RegisterResponse register(RegisterDTO registerRequest) {
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail().toLowerCase())
                .passwordHash(hashedPassword)
                .role(User.Role.USER)
                .isEmailVerified(false)
                .failedLoginAttempts(0)
                .accountLockedUntil(null)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        User saved = userRepository.save(user);

        return RegisterResponse.builder()
                .message("Registration successful")
                .username(saved.getUsername())
                .email(saved.getEmail())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public LoginResponse login(LoginDTO loginRequest) {
        final Instant now = Instant.now();
        final String email = loginRequest.getEmail().trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(email)
                .orElse(null);


        if (user == null) {
            return LoginResponse.builder()
                    .message("Invalid email or password")
                    .username(null)
                    .email(email)
                    .lastLoginAt(null)
                    .build();
        }

        // If account is locked
        if (user.getAccountLockedUntil() != null && now.isBefore(user.getAccountLockedUntil())) {

            userRepository.save(user);
            return LoginResponse.builder()
                    .message("Account is locked. Try again later.")
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .lastLoginAt(user.getLastLoginAt())
                    .build();
        }

        // Compare passwords
        boolean ok = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
        if (!ok) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);

            if (attempts >= MAX_ATTEMPTS) {
                user.setAccountLockedUntil(now.plus(Duration.ofMinutes(LOCK_MINUTES)));
            }

            userRepository.save(user);

            String msg = attempts >= MAX_ATTEMPTS
                    ? "Invalid email or password. Account locked for " + LOCK_MINUTES + " minutes."
                    : "Invalid email or password";

            return LoginResponse.builder()
                    .message(msg)
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .lastLoginAt(user.getLastLoginAt())
                    .build();
        }


        user.setAccountLockedUntil(null);
        user.setLastLoginAt(now);
        User saved = userRepository.save(user);


        String token = jwtService.generateToken(saved.getId());


        return LoginResponse.builder()
                .message("Login successful")
                .username(saved.getUsername())
                .email(saved.getEmail())
                .lastLoginAt(saved.getLastLoginAt())
                .token(token)
                .build();
    }


}
