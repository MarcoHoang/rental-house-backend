package com.codegym.service.impl;

import com.codegym.dto.response.ReviewDTO;
import com.codegym.entity.House;
import com.codegym.entity.Rental;
import com.codegym.entity.Review;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.ReviewRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Thay thế @Autowired bằng constructor injection
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final RentalRepository rentalRepository;

    private ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .reviewerId(review.getReviewer().getId()) // Đổi tên cho nhất quán
                .reviewerName(review.getReviewer().getUsername()) // Thêm thông tin hữu ích
                .houseId(review.getHouse().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .isVisible(review.getIsVisible())
                .createdAt(review.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đánh giá với ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByHouseId(Long houseId) {
        if (!houseRepository.existsById(houseId)) {
            throw new EntityNotFoundException("Không tìm thấy nhà với ID: " + houseId);
        }
        return reviewRepository.findByHouseIdAndIsVisibleTrueOrderByCreatedAtDesc(houseId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        User reviewer = userRepository.findById(reviewDTO.getReviewerId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID: " + reviewDTO.getReviewerId()));
        House house = houseRepository.findById(reviewDTO.getHouseId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy nhà với ID: " + reviewDTO.getHouseId()));

        // 1. (QUAN TRỌNG) Người dùng chỉ có thể đánh giá nhà họ đã thuê và đã trả phòng
         boolean hasRentedAndCheckedOut = rentalRepository.existsByRenterIdAndHouseIdAndStatus(reviewer.getId(), house.getId(), Rental.Status.CHECKED_OUT);
         if (!hasRentedAndCheckedOut) {
             throw new IllegalStateException("Bạn chỉ có thể đánh giá nhà bạn đã thuê và đã trả phòng.");
         }

        // 2. Mỗi người dùng chỉ được đánh giá một nhà một lần
        if (reviewRepository.existsByReviewerIdAndHouseId(reviewer.getId(), house.getId())) {
            throw new IllegalArgumentException("Bạn đã đánh giá nhà này rồi.");
        }

        Review review = new Review();
        review.setReviewer(reviewer);
        review.setHouse(house);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setIsVisible(true); // Mặc định khi tạo là hiển thị

        return toDTO(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        // 1. Lấy ra review đã tồn tại
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đánh giá để cập nhật với ID: " + id));

        // 2. (Tùy chọn) Kiểm tra quyền: Chỉ người viết ra review mới được sửa
        // if (!review.getReviewer().getId().equals(getCurrentUserId())) {
        //     throw new AccessDeniedException("Bạn không có quyền sửa đánh giá này.");
        // }

        // 3. Chỉ cập nhật các trường được phép thay đổi
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        // 4. Lưu lại entity đã được cập nhật
        return toDTO(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new EntityNotFoundException("Không thể xóa. Đánh giá với ID " + id + " không tồn tại.");
        }
        reviewRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ReviewDTO toggleVisibility(Long id) { // Đổi tên từ hideReview
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đánh giá với ID: " + id));

        review.setIsVisible(!review.getIsVisible()); // Đảo ngược trạng thái

        return toDTO(reviewRepository.save(review));
    }
}