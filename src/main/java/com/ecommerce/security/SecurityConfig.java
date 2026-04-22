package com.ecommerce.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // 🔥 FIX CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ================= PUBLIC =================
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // ================= PRODUCT =================
                        .requestMatchers("/api/products/**").permitAll()

                        // ================= CART =================
                        .requestMatchers("/api/cart/**")
                        .hasAnyRole("USER", "SUPPLIER")

                        // ================= ORDER =================
                        .requestMatchers("/api/orders/checkout").hasRole("USER")
                        .requestMatchers("/api/orders/my").hasRole("USER")
                        .requestMatchers("/api/orders/**")
                        .hasAnyRole("USER", "SUPPLIER")

                        // ================= PAYMENT =================
                        .requestMatchers("/api/payments/**").permitAll()

                        // ================= SUPPLIER =================
                        .requestMatchers("/api/products/create").hasRole("SUPPLIER")
                        .requestMatchers("/api/products/update/**").hasRole("SUPPLIER")
                        .requestMatchers("/api/products/delete/**").hasRole("SUPPLIER")

                        // ================= ADMIN =================
                        .requestMatchers("/api/supplier/**").hasRole("ADMIN")

                        // ================= DEFAULT =================
                        .anyRequest().authenticated()
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        jwtFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}