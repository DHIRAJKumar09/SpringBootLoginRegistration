package com.tcs.registration_login.userauthentication.controller;

import com.tcs.registration_login.userauthentication.dto.SignupRequest;
import com.tcs.registration_login.userauthentication.model.User;
import com.tcs.registration_login.userauthentication.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/me")
    public SignupRequest me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // or throw 401 - but endpoint is protected in security config
        }
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new SignupRequest(user.getUsername());
    }

}
