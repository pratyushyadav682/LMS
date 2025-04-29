//package com.cdac.backend.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import com.cdac.backend.dtos.LoginRequest;
//import com.cdac.backend.dtos.LoginResponse;
//import com.cdac.backend.dtos.SignupRequest;
//import com.cdac.backend.model.Role;
//import com.cdac.backend.model.User;
//import com.cdac.backend.repository.UserRepository;
//import com.cdac.backend.security.JwtUtil;
//
//@Service
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtUtil jwtUtil;
//
//    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jwtUtil = jwtUtil;
//    }
//
//    public String registerUser(SignupRequest request) {
//        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
//            return "Email already exists!";
//        }
//        User user = new User();
//        user.setName(request.getName());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(Role.USER);
//
//        userRepository.save(user);
//        return "User registered successfully!";
//    }
//
//    public LoginResponse loginUser(LoginRequest request) {
//        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
//        if (userOpt.isPresent() && passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
//            User user = userOpt.get();
//            String role = user.getRole().name(); // Get the role name (e.g., "USER", "ADMIN")
//            String token = jwtUtil.generateToken(request.getEmail());
//            return new LoginResponse(token, role);
//        }
//        return null; // Return null if authentication fails
//    }
//    
// 
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//    
//
//
//    // Method to remove a user by ID
//    public String removeUser(Long userId) {
//        if (userRepository.existsById(userId)) {
//            userRepository.deleteById(userId);
//            return "User deleted successfully!";
//        }
//        return "User not found!";
//    }
//}
package com.cdac.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cdac.backend.dtos.LoginRequest;
import com.cdac.backend.dtos.LoginResponse;
import com.cdac.backend.dtos.SignupRequest;
import com.cdac.backend.model.Role;
import com.cdac.backend.model.User;
import com.cdac.backend.repository.UserRepository;
import com.cdac.backend.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet(); // Store active user emails

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists!";
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        return "User registered successfully!";
    }

    public LoginResponse loginUser(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent() && passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            User user = userOpt.get();
            String role = user.getRole().name();
            String token = jwtUtil.generateToken(request.getEmail());

            activeUsers.add(user.getEmail()); // Add user to active list

            return new LoginResponse(token, role);
        }
        return null;
    }

    public boolean logoutUser(String email) {
        return activeUsers.remove(email); // Remove user from active list
    }

    public int getLoggedInUserCount() {
        return activeUsers.size(); // Get count of logged-in users
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public String removeUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return "User deleted successfully!";
        }
        return "User not found!";
    }
}

