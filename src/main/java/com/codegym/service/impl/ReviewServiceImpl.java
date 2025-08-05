
package com.codegym.service.impl;

import com.codegym.dto.response.ReviewDTO;
import com.codegym.entity.House;
import com.codegym.entity.Review;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.ReviewRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    private ReviewDTO toDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setReviewerId(review.getReviewer().getId());
        dto.setReviewerName(review.getReviewer().getUsername());
        dto.setHouseId(review.getHouse().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setIsVisible(review.getIsVisible());
        dto.setCreatedAt(review.getCreatedAt());
        return dto;
    }

    private Review toEntity(ReviewDTO dto, User reviewer, House house) {
        Review review = new Review();
        review.setId(dto.getId());
        review.setReviewer(reviewer);
        review.setHouse(house);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setIsVisible(dto.getIsVisible() != null ? dto.getIsVisible() : true);
        return review;
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        User reviewer = userRepository.findById(reviewDTO.getReviewerId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        House house = houseRepository.findById(reviewDTO.getHouseId())
                .orElseThrow(() -> new RuntimeException("House not found"));
        Review review = toEntity(reviewDTO, reviewer, house);
        review.setId(null);
        return toDTO(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
    @Override
    public ReviewDTO getReviewById(Long id) {
        return reviewRepository.findById(id).map(this::toDTO).orElse(null);
    }
    @Override
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        User reviewer = userRepository.findById(reviewDTO.getReviewerId())
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        House house = houseRepository.findById(reviewDTO.getHouseId())
                .orElseThrow(() -> new RuntimeException("House not found"));
        Review review = toEntity(reviewDTO, reviewer, house);
        review.setId(id);
        return toDTO(reviewRepository.save(review));
    }

    @Override
    public List<ReviewDTO> getReviewsByHouseId(Long houseId) {
        return reviewRepository.findAll().stream().filter(r -> r.getHouse().getId().equals(houseId)).map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ReviewDTO hideReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        review.setIsVisible(false);
        return toDTO(reviewRepository.save(review));
    }
}