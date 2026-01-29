package com.residentia.controller;

import com.residentia.dto.AuthResponseDTO;
import com.residentia.dto.LoginRequest;
import com.residentia.entity.Admin;
import com.residentia.entity.Owner;
import com.residentia.entity.RegularUser;
import com.residentia.repository.AdminRepository;
import com.residentia.repository.OwnerRepository;
import com.residentia.repository.RegularUserRepository;
import com.residentia.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private RegularUserRepository regularUserRepository;
    
    @Autowired
    private OwnerRepository ownerRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login attempt - Role: {}, Email: {}", loginRequest.getRole(), loginRequest.getEmail());
        
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        String role = loginRequest.getRole();
        
        Map<String, Object> response = new HashMap<>();
        
        // CLIENT LOGIN
        if ("CLIENT".equalsIgnoreCase(role)) {
            Optional<RegularUser> user = regularUserRepository.findByEmail(email);
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPasswordHash())) {
                String token = jwtTokenProvider.generateToken(user.get().getId().longValue(), email);
                
                AuthResponseDTO authResponse = new AuthResponseDTO();
                authResponse.setUserId(user.get().getId().longValue());
                authResponse.setEmail(user.get().getEmail());
                authResponse.setName(user.get().getName());
                authResponse.setToken(token);
                authResponse.setMessage("Client login successful!");
                
                log.info("Client login successful: {}", email);
                return ResponseEntity.ok(authResponse);
            }
        } 
        // OWNER LOGIN
        else if ("OWNER".equalsIgnoreCase(role)) {
            Optional<Owner> owner = ownerRepository.findByEmail(email);
            if (owner.isPresent() && passwordEncoder.matches(password, owner.get().getPasswordHash())) {
                String token = jwtTokenProvider.generateToken(owner.get().getId().longValue(), email);
                
                AuthResponseDTO authResponse = new AuthResponseDTO();
                authResponse.setOwnerId(owner.get().getId().longValue());
                authResponse.setEmail(owner.get().getEmail());
                authResponse.setName(owner.get().getName());
                authResponse.setToken(token);
                authResponse.setMessage("Owner login successful!");
                
                log.info("Owner login successful: {}", email);
                return ResponseEntity.ok(authResponse);
            }
        } 
        // ADMIN LOGIN
        else if ("ADMIN".equalsIgnoreCase(role)) {
            // Static admin credentials for login
            if ("admin@residentia.com".equals(email) && "Admin@123".equals(password)) {
                Optional<Admin> admin = adminRepository.findByEmail(email);
                if (admin.isPresent()) {
                    String token = jwtTokenProvider.generateToken(admin.get().getId().longValue(), email);
                    
                    AuthResponseDTO authResponse = new AuthResponseDTO();
                    authResponse.setAdminId(admin.get().getId().longValue());
                    authResponse.setEmail(admin.get().getEmail());
                    authResponse.setName(admin.get().getName());
                    authResponse.setToken(token);
                    authResponse.setMessage("Admin login successful!");
                    
                    log.info("Admin login successful: {}", email);
                    return ResponseEntity.ok(authResponse);
                }
            }
        }
        
        log.warn("Login failed for email: {} with role: {}", email, role);
        response.put("success", false);
        response.put("message", "Invalid email, password, or role");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}