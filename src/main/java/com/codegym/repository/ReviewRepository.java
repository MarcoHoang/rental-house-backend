package com.codegym.repository;

import com.codegym.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHouseIdAndIsVisibleTrueOrderByCreatedAtDesc(Long houseId);

    boolean existsByReviewerIdAndHouseId(Long reviewerId, Long houseId);
}
