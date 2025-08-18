package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
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
    private final MessageSource messageSource;

    @PostMapping("/{houseId}/toggle")
//    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<Boolean>> toggleFavorite(@PathVariable Long houseId, Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(StatusCode.UNAUTHORIZED.getCode(), "User not authenticated"));
        }
        
        try {
            Long userId = Long.parseLong(authentication.getName());
            boolean isFavorite = favoriteService.toggleFavorite(userId, houseId);
            String message = isFavorite ? "Đã thêm vào danh sách yêu thích" : "Đã bỏ khỏi danh sách yêu thích";
            
            return ResponseEntity.ok(ApiResponse.success(isFavorite, StatusCode.SUCCESS, messageSource, locale));
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(StatusCode.VALIDATION_ERROR.getCode(), "Invalid user ID format"));
        }
    }

    @GetMapping("/check/{houseId}")
//    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<Boolean>> checkFavorite(@PathVariable Long houseId, Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        boolean isFavorite = favoriteService.isFavorite(userId, houseId);
        return ResponseEntity.ok(ApiResponse.success(isFavorite, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/my-favorites")
//    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<List<Long>>> getMyFavoriteHouseIds(Locale locale) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        
        List<Long> favoriteHouseIds = favoriteService.getFavoriteHouseIds(userId);
        return ResponseEntity.ok(ApiResponse.success(favoriteHouseIds, StatusCode.SUCCESS, messageSource, locale));
    }

    @GetMapping("/{houseId}/count")
    public ResponseEntity<ApiResponse<Long>> getFavoriteCount(@PathVariable Long houseId, Locale locale) {
        Long count = favoriteService.getFavoriteCount(houseId);
        return ResponseEntity.ok(ApiResponse.success(count, StatusCode.SUCCESS, messageSource, locale));
    }
}
