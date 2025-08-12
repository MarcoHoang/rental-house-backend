package com.codegym.infrastructure;

import com.codegym.components.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Missing token");
                return;
            }

            final String token = authHeader.substring(7);
            final String email = jwtTokenUtil.extractUsername(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    Claims claims = jwtTokenUtil.extractClaims(token);
                    String role = claims.get("role", String.class);

                    List<SimpleGrantedAuthority> authorities = Collections.emptyList();
                    if (role != null && !role.isEmpty()) {
                        authorities = List.of(new SimpleGrantedAuthority(role));
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            logger.error("JWT Filter error: ", e);
            if (!response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Invalid token");
            }
        }
    }

    private boolean isBypassToken(@NotNull HttpServletRequest request) {

        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/auth/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/auth/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/admin/login", apiPrefix), "POST")
        );

        for (Pair<String, String> bypass : bypassTokens) {
            if (request.getServletPath().contains(bypass.getFirst()) &&
                    request.getMethod().equalsIgnoreCase(bypass.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
