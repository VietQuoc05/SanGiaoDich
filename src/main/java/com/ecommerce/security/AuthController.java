package com.ecommerce.security;

import com.ecommerce.security.dto.LoginRequest;
import com.ecommerce.security.dto.LoginResponse;
import com.ecommerce.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        return new LoginResponse(token);
    }

    @PostMapping("/register")
    public User register(@RequestBody LoginRequest request) {
        return authService.register(
                request.getUsername(),
                request.getPassword()
        );
    }
}