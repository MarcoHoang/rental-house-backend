package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.ReviewDTO;
import com.codegym.service.ReviewService;
import com.codegym.utils.MessageUtil;
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
    private final MessageUtil messageUtil;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        String message = messageUtil.getMessage("review.list.found");
        ApiResponse<List<ReviewDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, reviews);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/house/{houseId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByHouseId(@PathVariable Long houseId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByHouseId(houseId);
        String message = messageUtil.getMessage("review.list.byHouse.found");
        ApiResponse<List<ReviewDTO>> response = new ApiResponse<>(StatusCode.GET_LIST_SUCCESS.getCode(), message, reviews);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/toggle-visibility")
    public ResponseEntity<ApiResponse<ReviewDTO>> toggleReviewVisibility(@PathVariable Long id) {
        ReviewDTO dto = reviewService.toggleVisibility(id);
        String message = messageUtil.getMessage("review.visibility.toggled");
        ApiResponse<ReviewDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        String message = messageUtil.getMessage("review.get.found");
        ApiResponse<ReviewDTO> response = new ApiResponse<>(StatusCode.SUCCESS.getCode(), message, review);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO created = reviewService.createReview(reviewDTO);
        String message = messageUtil.getMessage("review.created");
        ApiResponse<ReviewDTO> response = new ApiResponse<>(StatusCode.CREATED_SUCCESS.getCode(), message, created);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updated = reviewService.updateReview(id, reviewDTO);
        String message = messageUtil.getMessage("review.updated");
        ApiResponse<ReviewDTO> response = new ApiResponse<>(StatusCode.UPDATED_SUCCESS.getCode(), message, updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        String message = messageUtil.getMessage("review.deleted");
        ApiResponse<Void> response = new ApiResponse<>(StatusCode.DELETED_SUCCESS.getCode(), message);
        return ResponseEntity.ok(response);
    }
}