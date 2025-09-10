package com.santiago.auth.service;

import com.santiago.auth.domain.User;
import com.santiago.auth.dto.LoginDTO;
import com.santiago.auth.dto.RegisterDTO;
import com.santiago.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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


    public User register(RegisterDTO registerRequest) {


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
        return userRepository.save(user);
    }

    public User login(LoginDTO loginRequest) {
        final Instant now = Instant.now();
        final String email = loginRequest.getEmail().trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify if account is locked
        if (user.getAccountLockedUntil() != null && now.isBefore(user.getAccountLockedUntil())) {
            throw new RuntimeException("Account is locked. Try again later.");
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
            throw new RuntimeException("Invalid email or password");
        }
        //Reset attempts on successful login
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        user.setLastLoginAt(now);

        return userRepository.save(user);
    }


}
