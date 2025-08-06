package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.ReviewDTO;
import com.codegym.service.ReviewService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, reviews));
    }

    @GetMapping("/house/{houseId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByHouseId(@PathVariable Long houseId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByHouseId(houseId);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, reviews));
    }

    @PutMapping("/{id}/toggle-visibility")
    public ResponseEntity<ApiResponse<ReviewDTO>> toggleReviewVisibility(@PathVariable Long id) {
        ReviewDTO dto = reviewService.toggleVisibility(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, review));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO created = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(new ApiResponse<>(StatusCode.SUCCESS, created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updated = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new ApiResponse<>(StatusCode.SUCCESS));
    }
}