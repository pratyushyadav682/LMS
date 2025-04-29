//package com.cdac.backend.controller;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.cdac.backend.dtos.LoginRequest;
//import com.cdac.backend.dtos.LoginResponse;
//import com.cdac.backend.dtos.SignupRequest;
//import com.cdac.backend.model.User;
//import com.cdac.backend.service.AuthService;
//
//@CrossOrigin(origins = "*")
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final AuthService authService;
//
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> registerUser(@RequestBody SignupRequest request) {
//        String response = authService.registerUser(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
//        LoginResponse response = authService.loginUser(request);
//        if (response != null) {
//            return ResponseEntity.ok(response);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//    }
//
//    // Get all users
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = authService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
//
//    // Remove a user by ID
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<String> removeUser(@PathVariable Long id) {
//        String response = authService.removeUser(id);
//        return ResponseEntity.ok(response);
//    }
//}
package com.cdac.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cdac.backend.dtos.LoginRequest;
import com.cdac.backend.dtos.LoginResponse;
import com.cdac.backend.dtos.SignupRequest;
import com.cdac.backend.model.User;
import com.cdac.backend.service.AuthService;

//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "http://localhost:5175", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // User Signup
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequest request) {
        String response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = authService.loginUser(request);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    // User Logout
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestParam String email) {
        boolean loggedOut = authService.logoutUser(email);
        if (loggedOut) {
            return ResponseEntity.ok("User logged out successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found in active sessions!");
    }

    // Get the count of logged-in users
    @GetMapping("/logged-in-users")
    public ResponseEntity<Integer> getLoggedInUserCount() {
        return ResponseEntity.ok(authService.getLoggedInUserCount());
    }

    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = authService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Remove a user by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> removeUser(@PathVariable Long id) {
        String response = authService.removeUser(id);
        return ResponseEntity.ok(response);
    }
}

