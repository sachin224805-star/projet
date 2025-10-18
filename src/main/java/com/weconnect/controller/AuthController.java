package com.weconnect.controller;

import com.weconnect.model.User;
import com.weconnect.repository.UserRepository;
import com.weconnect.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;
import com.weconnect.dto.AuthDTOs;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDTOs.Register body) {
        try {
            String name = body.name;
            String phone = body.phone;
            String password = body.password;

            if (userRepository.findByPhone(phone).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "User already exists"));
            }

            User user = new User();
            user.setName(name);
            user.setPhone(phone);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            String token = JwtUtil.generateToken(user.getId(), user.getRole());

            Map<String, Object> resp = new HashMap<>();
            resp.put("token", token);
            resp.put("user", Map.of("id", user.getId(), "name", user.getName(), "phone", user.getPhone(), "role", user.getRole()));

            return ResponseEntity.status(201).body(resp);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error creating user"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTOs.Login body) {
        try {
            String phone = body.phone;
            String password = body.password;
            var opt = userRepository.findByPhone(phone);
            if (opt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));

            User user = opt.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
            }

            String token = JwtUtil.generateToken(user.getId(), user.getRole());

            Map<String, Object> resp = new HashMap<>();
            resp.put("token", token);
            resp.put("user", Map.of("id", user.getId(), "name", user.getName(), "phone", user.getPhone(), "role", user.getRole()));

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error logging in"));
        }
    }
}
