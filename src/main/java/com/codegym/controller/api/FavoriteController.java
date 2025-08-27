package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.repository.UserRepository;
import com.codegym.service.FavoriteService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin("*")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @PostMapping("/{houseId}/toggle")
    public ResponseEntity<ApiResponse<Boolean>> toggleFavorite(@PathVariable Long houseId, Locale locale) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated() || 
                "anonymousUser".equals(authentication.getName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(StatusCode.UNAUTHORIZED.getCode(), 
                        "User not authenticated", messageSource, locale));
            }
            
            String userEmail = authentication.getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            boolean isFavorite = favoriteService.toggleFavorite(user.getId(), houseId);
            String message = isFavorite ? "Đã thêm vào danh sách yêu thích" : "Đã bỏ khỏi danh sách yêu thích";
            
            return ResponseEntity.ok(ApiResponse.success(isFavorite, StatusCode.SUCCESS, messageSource, locale));
        } catch (AppException e) {
            // Xử lý AppException với thông báo cụ thể
            String errorMessage = messageSource.getMessage(e.getStatusCode().getMessageKey(), null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getStatusCode().getCode(), 
                    errorMessage, messageSource, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                    "Internal server error: " + e.getMessage(), messageSource, locale));
        }
    }

    @GetMapping("/check/{houseId}")
    public ResponseEntity<ApiResponse<Boolean>> checkFavorite(@PathVariable Long houseId, Locale locale) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated() || 
                "anonymousUser".equals(authentication.getName())) {
                // Return false for unauthenticated users instead of error
                return ResponseEntity.ok(ApiResponse.success(false, StatusCode.SUCCESS, messageSource, locale));
            }
            
            String userEmail = authentication.getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            boolean isFavorite = favoriteService.isFavorite(user.getId(), houseId);
            return ResponseEntity.ok(ApiResponse.success(isFavorite, StatusCode.SUCCESS, messageSource, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                    "Internal server error: " + e.getMessage(), messageSource, locale));
        }
    }

    @GetMapping("/my-favorites")
    public ResponseEntity<ApiResponse<List<Long>>> getMyFavoriteHouseIds(Locale locale) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated() || 
                "anonymousUser".equals(authentication.getName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(StatusCode.UNAUTHORIZED.getCode(), 
                        "User not authenticated", messageSource, locale));
            }
            
            String userEmail = authentication.getName();
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            List<Long> favoriteHouseIds = favoriteService.getFavoriteHouseIds(user.getId());
            return ResponseEntity.ok(ApiResponse.success(favoriteHouseIds, StatusCode.SUCCESS, messageSource, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                    "Internal server error: " + e.getMessage(), messageSource, locale));
        }
    }

    @GetMapping("/{houseId}/count")
    public ResponseEntity<ApiResponse<Long>> getFavoriteCount(@PathVariable Long houseId, Locale locale) {
        try {
            Long count = favoriteService.getFavoriteCount(houseId);
            return ResponseEntity.ok(ApiResponse.success(count, StatusCode.SUCCESS, messageSource, locale));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                    "Internal server error: " + e.getMessage(), messageSource, locale));
        }
    }
}
