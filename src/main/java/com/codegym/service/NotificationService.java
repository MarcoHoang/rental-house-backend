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
    
    // Thông báo khi có đánh giá 1 sao
    void createReviewOneStarNotification(Long hostId, String userName, String houseName, Long reviewId, Long houseId);
}