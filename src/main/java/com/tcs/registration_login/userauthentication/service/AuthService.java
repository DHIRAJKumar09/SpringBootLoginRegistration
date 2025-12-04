package com.tcs.registration_login.userauthentication.service;

import com.tcs.registration_login.userauthentication.dto.AuthResponse;
import com.tcs.registration_login.userauthentication.dto.SignupRequest;
import com.tcs.registration_login.userauthentication.model.User;
import com.tcs.registration_login.userauthentication.repository.UserRepository;
import com.tcs.registration_login.userauthentication.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }
    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        List<String> roles = List.of(user.getRole().split(","));
        String token = tokenProvider.createToken(user.getUsername(), roles);

        return new AuthResponse(token);
    }

    public void register(SignupRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRole(req.getRole() == null ? "ROLE_USER" : req.getRole());
        user.setEnabled(true);
        user.setLocked(false);

        userRepository.save(user);
    }

}
