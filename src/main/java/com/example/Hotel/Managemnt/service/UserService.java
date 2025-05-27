package com.example.Hotel.Managemnt.service;

import com.example.Hotel.Managemnt.dto.LoginRequest;
import com.example.Hotel.Managemnt.entity.User;
import com.example.Hotel.Managemnt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;



    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Set role according to username
        String inputUsername = user.getUsername().toLowerCase();

        if (inputUsername.equals("admin")) {
            // Enforce only one admin
            boolean adminExists = userRepository.findByUserRoleIgnoreCase("ADMIN")
                    .stream()
                    .anyMatch(existing -> existing.getUsername().equalsIgnoreCase("admin"));

            if (adminExists) {
                throw new RuntimeException("Admin already registered");
            }

            user.setUserRole("ADMIN");

        } else if (inputUsername.equals("CLERK")) {
            user.setUserRole("CLERK");

        } else {
            user.setUserRole("USER");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedUser(user.getUsername());

        return userRepository.save(user);
    }

    //login
    public User login(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userOpt.get();
        System.out.println("Raw password: " + request.getPassword());
        System.out.println("Encoded password: " + user.getPassword());
        System.out.println("Match result: " + passwordEncoder.matches(request.getPassword(), user.getPassword()));


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }


        return user;
    }


    //delete
    public void deleteUser(String requestingUsername, Integer targetUserId) {
        Optional<User> requesterOpt = userRepository.findByUsername(requestingUsername);

        if (requesterOpt.isEmpty()) {
            throw new RuntimeException("Unauthorized action - requester not found");
        }

        User requester = requesterOpt.get();

        if (!"ADMIN".equalsIgnoreCase(requester.getUserRole())) {
            throw new RuntimeException("Only admin is allowed to delete users");
        }

        if (!userRepository.existsById(targetUserId)) {
            throw new RuntimeException("Target user not found");
        }

        userRepository.deleteById(targetUserId);
    }

}
