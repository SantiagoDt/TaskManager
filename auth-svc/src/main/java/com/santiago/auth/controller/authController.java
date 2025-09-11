package com.santiago.auth.controller;

import com.santiago.auth.domain.User;
import com.santiago.auth.dto.LoginDTO;
import com.santiago.auth.dto.LoginResponse;
import com.santiago.auth.dto.RegisterDTO;
import com.santiago.auth.dto.RegisterResponse;
import com.santiago.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class authController {
    @Autowired
    private AuthService authService;
    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterDTO registerRequest){
        return authService.register(registerRequest);
    }
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginDTO loginRequest){
        return authService.login(loginRequest);
    }
}
