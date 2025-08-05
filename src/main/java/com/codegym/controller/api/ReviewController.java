package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.ReviewDTO;
import com.codegym.service.ReviewService;
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

    // Lấy tất cả đánh giá (có thể cần cho admin)
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy danh sách đánh giá thành công", reviews));
    }

    // Lấy đánh giá theo houseId
    @GetMapping("/house/{houseId}")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getReviewsByHouseId(@PathVariable Long houseId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByHouseId(houseId);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy đánh giá theo nhà thành công", reviews));
    }

    // Ẩn/Hiện đánh giá
    @PutMapping("/{id}/toggle-visibility")
    public ResponseEntity<ApiResponse<ReviewDTO>> toggleReviewVisibility(@PathVariable Long id) {
        ReviewDTO dto = reviewService.toggleVisibility(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Thay đổi trạng thái hiển thị thành công", dto));
    }

    // Lấy đánh giá theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin đánh giá thành công", review));
    }

    // Tạo mới đánh giá
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDTO>> createReview(@Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO created = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(new ApiResponse<>(201, "Tạo đánh giá mới thành công", created), HttpStatus.CREATED);
    }

    // Cập nhật đánh giá
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewDTO>> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updated = reviewService.updateReview(id, reviewDTO);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật đánh giá thành công", updated));
    }

    // Xóa đánh giá
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa đánh giá thành công", null));
    }
}