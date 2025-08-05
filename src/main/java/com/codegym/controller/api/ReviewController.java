package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.ReviewDTO;
import com.codegym.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin("*")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Lấy tất cả đánh giá
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(ApiResponse.success("Lấy danh sách đánh giá thành công", reviews));
    }

    // Lấy đánh giá theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        if (review == null) {
            return ResponseEntity.ok(ApiResponse.error("404", "Không tìm thấy đánh giá với ID = " + id));
        }
        return ResponseEntity.ok(ApiResponse.success("Lấy thông tin đánh giá thành công", review));
    }

    // Tạo mới đánh giá
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO created = reviewService.createReview(reviewDTO);
        return ResponseEntity.ok(ApiResponse.success("Tạo đánh giá mới thành công", created));
    }

    // Cập nhật đánh giá
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updated = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(ApiResponse.success("Cập nhật đánh giá thành công", updated));
    }

    // Xóa đánh giá
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa đánh giá thành công", null));
    }
}
