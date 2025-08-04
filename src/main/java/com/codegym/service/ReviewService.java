package com.codegym.service;

import com.codegym.dto.response.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReviews();
    ReviewDTO createReview(ReviewDTO reviewDTO);

    void deleteReview(Long id);

    ReviewDTO getReviewById(Long id);
    ReviewDTO updateReview(Long id, ReviewDTO reviewDTO);
}
