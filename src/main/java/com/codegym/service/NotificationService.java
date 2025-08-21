package com.codegym.service;

import com.codegym.dto.response.NotificationDTO;
import java.util.List;

public interface NotificationService {

    List<NotificationDTO> findByReceiverId(Long userId);

    NotificationDTO create(NotificationDTO dto);

    NotificationDTO markAsRead(Long id);

    void deleteById(Long id);
    
    void createHouseDeletedNotification(Long hostId, String houseTitle, Long houseId);
    
    // Thông báo khi có yêu cầu thuê mới
    void createRentalRequestNotification(Long hostId, String userName, String houseName, Long rentalId, Long houseId);
    
    // Thông báo khi khách đặt thuê nhà
    void createRentalBookedNotification(Long hostId, String userName, String houseName, Long rentalId, Long houseId);
    
    // Thông báo khi khách hủy thuê nhà
    void createRentalCanceledNotification(Long hostId, String userName, String houseName, Long rentalId, Long houseId);
    
    // Thông báo khi có đánh giá 1-2 sao (đánh giá tệ)
    void createReviewLowRatingNotification(Long hostId, String userName, String houseName, Long reviewId, Long houseId, Integer rating);
    
    // Thông báo khi có đánh giá 3 sao (đánh giá bình thường)
    void createReviewMediumRatingNotification(Long hostId, String userName, String houseName, Long reviewId, Long houseId);
    
    // Thông báo khi có đánh giá 4-5 sao (đánh giá cao)
    void createReviewHighRatingNotification(Long hostId, String userName, String houseName, Long reviewId, Long houseId, Integer rating);
}