package com.tcs.registration_login.userauthentication.config;

import com.tcs.registration_login.userauthentication.security.CustomUserDetailsService;
import com.tcs.registration_login.userauthentication.security.JwtAuthenticaitonFilter;
import com.tcs.registration_login.userauthentication.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticaitonFilter jwtFilter = new JwtAuthenticaitonFilter(tokenProvider);

        http
        // Disable CSRF for APIs
        .csrf(csrf -> csrf.disable())
                // Make session stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure endpoint access rules
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()         // public endpoints
                        .requestMatchers("/h2-console/**").permitAll()  // H2 console in dev
                        .anyRequest().authenticated()                    // all other endpoints require auth
                )
                // Allow frames for H2 console
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // Add JWT filter before Spring Security authentication filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
