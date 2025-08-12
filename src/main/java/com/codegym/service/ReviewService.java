package com.codegym.service;

import com.codegym.dto.response.ReviewDTO;
import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getAllReviews();

    ReviewDTO getReviewById(Long id);

    List<ReviewDTO> getReviewsByHouseId(Long houseId);

    ReviewDTO createReview(ReviewDTO reviewDTO);

    ReviewDTO updateReview(Long id, ReviewDTO reviewDTO);

    void deleteReview(Long id);

    ReviewDTO toggleVisibility(Long id);
}