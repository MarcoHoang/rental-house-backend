package com.codegym.config;


import com.codegym.infrastructure.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
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
                                // Cho phép không cần login
                                .requestMatchers(
                                        String.format("%s/auth/register", apiPrefix),
                                        String.format("%s/auth/login", apiPrefix),
                                        String.format("%s/admin/login", apiPrefix), // Admin login
                                        String.format("%s/users/password-reset/**", apiPrefix),
                                        String.format("%s/houses", apiPrefix),
                                        String.format("%s/houses/*", apiPrefix),
                                        String.format("%s/houses/top", apiPrefix),
                                        String.format("%s/houses/search", apiPrefix),
                                        String.format("%s/houses/*/images", apiPrefix),
                                        String.format("%s/files/uploads/avatar", apiPrefix),
                                        String.format("%s/files/**", apiPrefix) // File access
                                ).permitAll()


                                // Người dùng (ROLE_USER) - cho phép cả ADMIN và HOST truy cập
                                .requestMatchers(
                                        String.format("%s/users/*/profile", apiPrefix),
                                        String.format("%s/users/profile", apiPrefix),
                                        String.format("%s/users/*/change-password", apiPrefix),
                                        String.format("%s/rentals", apiPrefix),
                                        String.format("%s/rentals/*", apiPrefix),
                                        String.format("%s/reviews", apiPrefix),
                                        String.format("%s/notifications", apiPrefix),
                                        String.format("%s/chat/**", apiPrefix),
                                        String.format("%s/favorites/**", apiPrefix)
                                ).hasAnyRole("USER", "ADMIN", "HOST")

//                         Quản trị viên (ROLE_ADMIN)
                                .requestMatchers(
                                        String.format("%s/users", apiPrefix),
                                        String.format("%s/users/*", apiPrefix),
                                        String.format("%s/renters", apiPrefix),
                                        String.format("%s/renters/*", apiPrefix),
                                        String.format("%s/renter-requests", apiPrefix),
                                        String.format("%s/dashboard/**", apiPrefix),
                                        String.format("%s/banners", apiPrefix),
                                        String.format("%s/admin/dashboard", apiPrefix),
                                        String.format("%s/admin/users/**", apiPrefix)
                                ).hasRole("ADMIN")



                                // Chủ nhà (ROLE_HOST) - các endpoint chỉ dành cho HOST
                                .requestMatchers(
                                        String.format("%s/users/*/change-password", apiPrefix),
                                        String.format("%s/hosts/change-password", apiPrefix),
                                        String.format("%s/users/is-host", apiPrefix),
                                        String.format("%s/users/host-info", apiPrefix),
                                        String.format("%s/houses/my-houses", apiPrefix),
                                        String.format("%s/hosts/my-stats", apiPrefix),
                                        String.format("%s/hosts/my-profile", apiPrefix),
                                        String.format("%s/hosts/my-profile/**", apiPrefix),
                                        String.format("%s/houses/*/edit", apiPrefix), // Sửa nhà của mình
                                        String.format("%s/houses/*/delete", apiPrefix), // Xóa nhà của mình
                                        String.format("%s/houses/*/status", apiPrefix), // Thay đổi trạng thái
                                        String.format("%s/renters/*/houses", apiPrefix),
                                        String.format("%s/renters/*/rentals", apiPrefix),
                                        String.format("%s/renters/*/income", apiPrefix),
                                        String.format("%s/reviews/*/hide", apiPrefix),
                                        String.format("%s/house-images", apiPrefix),
                                        String.format("%s/renter-requests", apiPrefix),
                                        String.format("%s/notifications", apiPrefix),
                                        String.format("%s/renters/*/checkin", apiPrefix),
                                        String.format("%s/renters/*/checkout", apiPrefix),
                                        String.format("%s/renters/*/statistics", apiPrefix)
                                ).hasRole("HOST")

                                // Admin có thể xóa nhà thông qua endpoint riêng (nếu cần)
                                // .requestMatchers(
                                //         String.format("%s/admin/houses/*/delete", apiPrefix)
                                // ).hasRole("ADMIN")

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
