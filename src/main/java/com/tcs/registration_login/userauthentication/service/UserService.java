package com.tcs.registration_login.userauthentication.service;

import com.tcs.registration_login.userauthentication.model.User;
import com.tcs.registration_login.userauthentication.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public  UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public User updateRole(String username, String newRole) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(newRole);
        return userRepository.save(user);
    }
    public User deactivateUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(false);
        return userRepository.save(user);
    }
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }
}
