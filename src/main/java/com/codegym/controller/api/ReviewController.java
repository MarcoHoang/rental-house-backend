package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.ReviewDTO;
import com.codegym.service.ReviewService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReviewController {

    private final ReviewService reviewService;
    private final MessageSource messageSource;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getAllReviews(Locale locale) {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.success(reviews, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @GetMapping("/house/{houseId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByHouseId(@PathVariable Long houseId, Locale locale) {
        List<ReviewDTO> reviews = reviewService.getReviewsByHouseId(houseId);
        return ResponseEntity.ok(ApiResponse.success(reviews, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
    }

    @PutMapping("/{id}/toggle-visibility")
    public ResponseEntity<ApiResponse<ReviewDTO>> toggleReviewVisibility(@PathVariable Long id, Locale locale) {
        ReviewDTO dto = reviewService.toggleVisibility(id);
        return ResponseEntity.ok(ApiResponse.success(dto, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> getReviewById(@PathVariable Long id, Locale locale) {
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(ApiResponse.success(review, StatusCode.SUCCESS, messageSource, locale));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody ReviewDTO reviewDTO, Locale locale) {
        ReviewDTO created = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(ApiResponse.success(created, StatusCode.CREATED_SUCCESS, messageSource, locale), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO, Locale locale) {
        ReviewDTO updated = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, StatusCode.UPDATED_SUCCESS, messageSource, locale));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id, Locale locale) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success(StatusCode.DELETED_SUCCESS, messageSource, locale));
    }
}