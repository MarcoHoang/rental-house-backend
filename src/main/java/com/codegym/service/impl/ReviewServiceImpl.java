package com.codegym.service.impl;

import com.codegym.dto.request.CreateReviewRequest;
import com.codegym.dto.response.ReviewDTO;
import com.codegym.entity.House;
import com.codegym.entity.Rental;
import com.codegym.entity.Review;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.RentalRepository;
import com.codegym.repository.ReviewRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.ReviewService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final RentalRepository rentalRepository;

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
        return reviewRepository.findByHouseIdAndIsVisibleTrueOrderByCreatedAtDesc(houseId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDTO createReview(CreateReviewRequest request) {
        User reviewer = findUserByIdOrThrow(request.getReviewerId());
        House house = findHouseByIdOrThrow(request.getHouseId());

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

        return toDTO(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = findReviewByIdOrThrow(id);

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        return toDTO(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = findReviewByIdOrThrow(id);
        reviewRepository.delete(review);
    }

    @Override
    @Transactional
    public ReviewDTO toggleVisibility(Long id) {
        Review review = findReviewByIdOrThrow(id);
        review.setIsVisible(!review.getIsVisible());
        return toDTO(reviewRepository.save(review));
    }
}