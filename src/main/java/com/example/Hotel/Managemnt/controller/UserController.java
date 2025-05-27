package com.example.Hotel.Managemnt.controller;


import com.example.Hotel.Managemnt.dto.LoginRequest;
import com.example.Hotel.Managemnt.entity.User;
import com.example.Hotel.Managemnt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User savedUser = userService.registerUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.login(loginRequest);
            return ResponseEntity.ok("Login successful for user: " + user.getUsername());
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable("id") Integer id,
            @RequestParam("requestedBy") String username) {
        try {
            userService.deleteUser(username, id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }


}