package com.ecommerce.security;

import com.ecommerce.security.dto.LoginRequest;
import com.ecommerce.security.dto.LoginResponse;
import com.ecommerce.user.User;
import com.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // 🔥 LOGIN
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow();

        // 🔥 FIX: truyền role vào token
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole()
        );

        return new LoginResponse(
                token,
                user.getRole()
        );
    }

    // 🔥 REGISTER
    public User register(String username, String password) {

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER") // ⚠️ phải có ROLE_
                .supplierRequest(false)
                .build();

        return userRepo.save(user);
    }
}