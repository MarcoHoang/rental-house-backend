package com.codegym.service.impl;

import com.codegym.dto.request.CreateReviewRequest;
import com.codegym.dto.response.ReviewDTO;
import com.codegym.entity.House;
import com.codegym.entity.Rental;
import com.codegym.entity.Review;
import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.ReviewRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.ReviewService;
import com.codegym.service.NotificationService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final RentalRepository rentalRepository;
    private final NotificationService notificationService;

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, userId));
    }

    private House findHouseByIdOrThrow(Long houseId) {
        return houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOUSE_NOT_FOUND, houseId));
    }

    private Review findReviewByIdOrThrow(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.REVIEW_NOT_FOUND, reviewId));
    }

    private User getCurrentUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, currentUsername));
    }

    private void checkReviewOwnership(Review review) {
        User currentUser = getCurrentUser();
        
        // Admin có thể quản lý tất cả đánh giá
        if (currentUser.getRole().getName().equals(RoleName.ADMIN)) {
            return;
        }
        
        // Chủ nhà có thể quản lý đánh giá của nhà mình
        if (currentUser.getRole().getName().equals(RoleName.HOST) && 
            Objects.equals(review.getHouse().getHost().getId(), currentUser.getId())) {
            return;
        }
        
        // Người dùng thường chỉ có thể quản lý đánh giá của mình
        if (!Objects.equals(review.getReviewer().getId(), currentUser.getId())) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Bạn không có quyền chỉnh sửa hoặc xóa đánh giá này");
        }
    }

    private ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .reviewerId(review.getReviewer().getId())
                .reviewerName(review.getReviewer().getUsername())
                .reviewerFullName(review.getReviewer().getFullName())
                .reviewerAvatarUrl(review.getReviewer().getAvatarUrl())
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
        Review review = findReviewByIdOrThrow(id);
        return toDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByHouseId(Long houseId) {
        findHouseByIdOrThrow(houseId);
        List<Review> reviews = reviewRepository.findByHouseIdAndIsVisibleTrueOrderByCreatedAtDesc(houseId);
        System.out.println("=== getReviewsByHouseId Debug ===");
        System.out.println("House ID: " + houseId);
        System.out.println("Found " + reviews.size() + " visible reviews");
        return reviews.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getAllReviewsByHouseId(Long houseId) {
        House house = findHouseByIdOrThrow(houseId);
        User currentUser = getCurrentUser();
        
        System.out.println("=== getAllReviewsByHouseId Debug ===");
        System.out.println("House ID: " + houseId);
        System.out.println("House Host ID: " + house.getHost().getId());
        System.out.println("Current User ID: " + currentUser.getId());
        System.out.println("Current User Role: " + currentUser.getRole().getName());
        System.out.println("Is Admin: " + currentUser.getRole().getName().equals(RoleName.ADMIN));
        System.out.println("Is Host Owner: " + Objects.equals(house.getHost().getId(), currentUser.getId()));
        
        // Chỉ ADMIN hoặc chủ nhà của nhà này mới có thể xem tất cả reviews (bao gồm ẩn)
        if (!currentUser.getRole().getName().equals(RoleName.ADMIN) && 
            !Objects.equals(house.getHost().getId(), currentUser.getId())) {
            System.out.println("Access denied - User is not admin and not house owner");
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Bạn không có quyền xem tất cả đánh giá của nhà này");
        }
        
        System.out.println("Access granted - Fetching all reviews");
        List<Review> reviews = reviewRepository.findByHouseIdOrderByCreatedAtDesc(houseId);
        System.out.println("Found " + reviews.size() + " reviews");
        return reviews.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDTO createReview(CreateReviewRequest request) {
        User reviewer = findUserByIdOrThrow(request.getReviewerId());
        House house = findHouseByIdOrThrow(request.getHouseId());

        // Kiểm tra xem người dùng hiện tại có phải là người tạo đánh giá không
        User currentUser = getCurrentUser();
        
        // Admin có thể tạo đánh giá cho bất kỳ ai, người dùng thường chỉ có thể tạo đánh giá cho chính mình
        if (!currentUser.getRole().getName().equals(RoleName.ADMIN) && !currentUser.getId().equals(reviewer.getId())) {
            throw new AppException(StatusCode.FORBIDDEN_ACTION, "Bạn chỉ có thể tạo đánh giá cho chính mình");
        }

        // Tạm thời bỏ điều kiện phải thuê nhà và checkout
        // boolean hasRentedAndCheckedOut = rentalRepository.existsByRenterIdAndHouseIdAndStatus(reviewer.getId(), house.getId(), Rental.Status.CHECKED_OUT);
        // if (!hasRentedAndCheckedOut) {
        //     throw new AppException(StatusCode.CANNOT_REVIEW_NOT_RENTED);
        // }

        if (reviewRepository.existsByReviewerIdAndHouseId(reviewer.getId(), house.getId())) {
            throw new AppException(StatusCode.REVIEW_ALREADY_EXISTS);
        }

        Review review = new Review();
        review.setReviewer(reviewer);
        review.setHouse(house);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setIsVisible(true);

        Review savedReview = reviewRepository.save(review);
        
        // Tạo thông báo cho host dựa trên mức đánh giá
        try {
            if (request.getRating() <= 2) {
                // Đánh giá 1-2 sao: Nhà bị đánh giá tệ
                notificationService.createReviewLowRatingNotification(
                    house.getHost().getId(),
                    reviewer.getFullName(),
                    house.getTitle(),
                    savedReview.getId(),
                    house.getId(),
                    request.getRating()
                );
            } else if (request.getRating() == 3) {
                // Đánh giá 3 sao: Nhà được đánh giá bình thường
                notificationService.createReviewMediumRatingNotification(
                    house.getHost().getId(),
                    reviewer.getFullName(),
                    house.getTitle(),
                    savedReview.getId(),
                    house.getId()
                );
            } else if (request.getRating() >= 4) {
                // Đánh giá 4-5 sao: Nhà được đánh giá cao, có khả năng thuê lại
                notificationService.createReviewHighRatingNotification(
                    house.getHost().getId(),
                    reviewer.getFullName(),
                    house.getTitle(),
                    savedReview.getId(),
                    house.getId(),
                    request.getRating()
                );
            }
        } catch (Exception e) {
            // Log lỗi nhưng không throw để tránh rollback transaction chính
            System.err.println("Failed to create review notification: " + e.getMessage());
        }
        
        return toDTO(savedReview);
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = findReviewByIdOrThrow(id);
        
        // Kiểm tra quyền sở hữu đánh giá
        checkReviewOwnership(review);

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        Review savedReview = reviewRepository.save(review);
        
        // Tạo thông báo cho host dựa trên mức đánh giá khi cập nhật
        try {
            if (reviewDTO.getRating() <= 2) {
                // Đánh giá 1-2 sao: Nhà bị đánh giá tệ
                notificationService.createReviewLowRatingNotification(
                    review.getHouse().getHost().getId(),
                    review.getReviewer().getFullName(),
                    review.getHouse().getTitle(),
                    savedReview.getId(),
                    review.getHouse().getId(),
                    reviewDTO.getRating()
                );
            } else if (reviewDTO.getRating() == 3) {
                // Đánh giá 3 sao: Nhà được đánh giá bình thường
                notificationService.createReviewMediumRatingNotification(
                    review.getHouse().getHost().getId(),
                    review.getReviewer().getFullName(),
                    review.getHouse().getTitle(),
                    savedReview.getId(),
                    review.getHouse().getId()
                );
            } else if (reviewDTO.getRating() >= 4) {
                // Đánh giá 4-5 sao: Nhà được đánh giá cao, có khả năng thuê lại
                notificationService.createReviewHighRatingNotification(
                    review.getHouse().getHost().getId(),
                    review.getReviewer().getFullName(),
                    review.getHouse().getTitle(),
                    savedReview.getId(),
                    review.getHouse().getId(),
                    reviewDTO.getRating()
                );
            }
        } catch (Exception e) {
            // Log lỗi nhưng không throw để tránh rollback transaction chính
            System.err.println("Failed to create review update notification: " + e.getMessage());
        }

        return toDTO(savedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = findReviewByIdOrThrow(id);
        
        // Kiểm tra quyền sở hữu đánh giá
        checkReviewOwnership(review);
        
        reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public ReviewDTO toggleVisibility(Long id) {
        Review review = findReviewByIdOrThrow(id);
        
        // Kiểm tra quyền sở hữu đánh giá
        checkReviewOwnership(review);
        
        review.setIsVisible(!review.getIsVisible());
        Review savedReview = reviewRepository.save(review);
        
        return toDTO(savedReview);
    }
}