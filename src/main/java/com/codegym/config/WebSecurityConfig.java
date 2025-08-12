package com.codegym.config;


import com.codegym.infrastructure.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // ================== ENDPOINTS CÔNG KHAI ==================
                        .requestMatchers(
                                String.format("%s/auth/**", apiPrefix),
                                String.format("%s/admin/login", apiPrefix)
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/houses", apiPrefix),
                                String.format("%s/houses/**", apiPrefix),
                                String.format("%s/files/**", apiPrefix)
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/host-requests", apiPrefix),
                                String.format("%s/hosts", apiPrefix),
                                String.format("%s/users", apiPrefix)
                        ).hasRole("ADMIN")
                        .requestMatchers(
                                String.format("%s/host-requests/*/approve", apiPrefix),
                                String.format("%s/host-requests/*/reject", apiPrefix),
                                String.format("%s/hosts/**", apiPrefix), // Bao gồm cả lock/unlock, delete,...
                                String.format("%s/users/**", apiPrefix),
                                String.format("%s/dashboard/**", apiPrefix),
                                String.format("%s/banners/**", apiPrefix)
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, String.format("%s/host-requests", apiPrefix)).hasRole("USER")
                        .requestMatchers(HttpMethod.GET, String.format("%s/host-requests/my-request", apiPrefix)).hasRole("USER")
                        .requestMatchers(
                                String.format("%s/users/profile", apiPrefix),
                                String.format("%s/users/*/change-password", apiPrefix),
                                String.format("%s/rentals", apiPrefix),
                                String.format("%s/reviews", apiPrefix)
                        ).hasRole("USER")


                        .requestMatchers(
                                String.format("%s/rentals/host/**", apiPrefix),
                                String.format("%s/reviews/*/hide", apiPrefix),
                                String.format("%s/house-images/**", apiPrefix),
                                String.format("%s/houses/*/status", apiPrefix),
                                String.format("%s/rentals/*/checkin", apiPrefix),
                                String.format("%s/rentals/*/checkout", apiPrefix)
                        ).hasRole("HOST")

                        .requestMatchers(HttpMethod.POST, String.format("%s/houses", apiPrefix)).hasAnyRole("USER", "HOST")
                        .requestMatchers(HttpMethod.PUT, String.format("%s/houses/*", apiPrefix)).hasAnyRole("USER", "HOST")
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/houses/*", apiPrefix)).hasAnyRole("USER", "HOST")

                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));
        config.setExposedHeaders(List.of("Authorization", "x-auth-token"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json");
            response.setStatus(401);
            response.getWriter().write("{\"error\":\"Unauthorized\"}");
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json");
            response.setStatus(403);
            response.getWriter().write("{\"error\":\"Forbidden\"}");
        };
    }
}

