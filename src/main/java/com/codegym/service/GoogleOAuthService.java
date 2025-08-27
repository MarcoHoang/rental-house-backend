package com.codegym.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.codegym.dto.request.GoogleLoginRequest;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.repository.UserRepository;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService {

    private final UserRepository userRepository;
    
    @Value("${google.oauth.client-id}")
    private String googleClientId;

    public GoogleIdToken.Payload verifyGoogleToken(GoogleLoginRequest request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getIdToken());
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                
                // Verify that the token was issued for your app
                if (!googleClientId.equals(payload.getAudience())) {
                    log.error("Token audience mismatch. Expected: {}, Got: {}", googleClientId, payload.getAudience());
                    throw new AppException(StatusCode.INVALID_CREDENTIALS);
                }
                
                return payload;
            } else {
                log.error("Invalid Google ID token");
                throw new AppException(StatusCode.INVALID_CREDENTIALS);
            }
        } catch (Exception e) {
            log.error("Error verifying Google token", e);
            throw new AppException(StatusCode.INVALID_CREDENTIALS);
        }
    }

    public User findOrCreateUser(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        String googleAccountId = payload.getSubject();
        
        // First try to find user by Google account ID
        User user = userRepository.findByGoogleAccountId(googleAccountId).orElse(null);
        
        if (user == null) {
            // If not found by Google ID, try by email
            user = userRepository.findByEmail(email).orElse(null);
            
            if (user != null) {
                // User exists but doesn't have Google account linked
                // Link the Google account
                user.setGoogleAccountId(googleAccountId);
                user.setActive(true);
                user = userRepository.save(user);
                log.info("Linked Google account to existing user: {}", email);
            } else {
                // Create new user
                user = createNewGoogleUser(payload);
                log.info("Created new user from Google OAuth: {}", email);
            }
        }
        
        return user;
    }

    private User createNewGoogleUser(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        String googleAccountId = payload.getSubject();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");
        
        // Generate a random password for Google users
        String randomPassword = generateRandomPassword();
        
        User user = User.builder()
                .email(email)
                .username(email) // Use email as username for Google users
                .fullName(name != null ? name : email.split("@")[0])
                .phone("0000000000") // Default phone for Google users - they need to update it
                .password(randomPassword) // This will be encoded in the service
                .googleAccountId(googleAccountId)
                .avatarUrl(picture != null ? picture : "/images/default-avatar.png")
                .active(true)
                .build();
        
        return user;
    }

    private String generateRandomPassword() {
        // Generate a secure random password for Google users
        return "GOOGLE_" + System.currentTimeMillis() + "_" + Math.random();
    }
} 