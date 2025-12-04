package com.tcs.registration_login.userauthentication.controller;

import com.tcs.registration_login.userauthentication.dto.AuthResponse;
import com.tcs.registration_login.userauthentication.dto.LoginRequest;
import com.tcs.registration_login.userauthentication.dto.SignupRequest;
import com.tcs.registration_login.userauthentication.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest req) {
        AuthResponse resp = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid SignupRequest req) {
        authService.register(req);
        return ResponseEntity.ok("User registered");
    }
}
