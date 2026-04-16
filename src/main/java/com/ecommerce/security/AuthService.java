package com.ecommerce.security;

import com.ecommerce.security.dto.LoginRequest;
import com.ecommerce.user.User;
import com.ecommerce.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // 🔥 LOGIN
    public String login(LoginRequest request) {

        // authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // generate token
        return jwtUtil.generateToken(request.getUsername());
    }

    // 🔥 REGISTER
    public User register(String username, String password) {

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role("ROLE_USER")
                .supplierRequest(false)
                .build();

        return userRepository.save(user); // 🔥 RETURN USER
    }
}