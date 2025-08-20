package com.codegym.service.impl;

import com.codegym.dto.response.NotificationDTO;
import com.codegym.entity.Notification;
import com.codegym.entity.User;
import com.codegym.entity.House;
import com.codegym.entity.Review;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.NotificationRepository;
import com.codegym.repository.UserRepository;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.ReviewRepository;
import com.codegym.service.EmailService;
import com.codegym.service.NotificationService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final ReviewRepository reviewRepository;
    private final EmailService emailService;
    private final MessageSource messageSource;

    private User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, userId));
    }

    private Notification findNotificationByIdOrThrow(Long notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.NOTIFICATION_NOT_FOUND, notificationId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> findByReceiverId(Long userId) {
        findUserByIdOrThrow(userId);
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationDTO create(NotificationDTO dto) {
        User receiver = findUserByIdOrThrow(dto.getReceiverId());

        Notification notification = Notification.builder()
                .receiver(receiver)
                .type(dto.getType())
                .content(dto.getContent())
                .isRead(false)
                .build();

        return toDTO(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public NotificationDTO markAsRead(Long id) {
        Notification notification = findNotificationByIdOrThrow(id);

        if (Boolean.TRUE.equals(notification.getIsRead())) {
            return toDTO(notification);
        }

        notification.setIsRead(true);
        return toDTO(notificationRepository.save(notification));
    }



    @Override
    @Transactional
    public void deleteById(Long id) {
        Notification notification = findNotificationByIdOrThrow(id);
        notificationRepository.delete(notification);
    }

    @Override
    @Transactional
    public void createHouseDeletedNotification(Long hostId, String houseTitle, Long houseId) {
        try {
            log.info("Creating house deleted notification - hostId: {}, houseTitle: {}, houseId: {}", hostId, houseTitle, houseId);
            
            User host = findUserByIdOrThrow(hostId);
            log.info("Found host user: {} (email: {})", host.getId(), host.getEmail());
            
            // Tạo notification trong database
            String notificationContent;
            try {
                // Đảm bảo houseTitle không null
                String safeHouseTitle = (houseTitle != null && !houseTitle.trim().isEmpty()) 
                    ? houseTitle.trim() 
                    : "Nhà #" + houseId;
                
                log.info("Using house title: '{}' for notification", safeHouseTitle);
                
                notificationContent = messageSource.getMessage(
                    "notification.house.deleted", 
                    new Object[]{safeHouseTitle}, 
                    Locale.getDefault()
                );
                log.info("Notification content: {}", notificationContent);
            } catch (Exception e) {
                log.error("Failed to get message from messageSource: {}", e.getMessage());
                String safeHouseTitle = (houseTitle != null && !houseTitle.trim().isEmpty()) 
                    ? houseTitle.trim() 
                    : "Nhà #" + houseId;
                notificationContent = "Nhà '" + safeHouseTitle + "' đã bị xóa khỏi hệ thống bởi quản trị viên.";
                log.info("Using fallback notification content: {}", notificationContent);
            }
            
            Notification notification = Notification.builder()
                    .receiver(host)
                    .type(Notification.Type.HOUSE_DELETED)
                    .content(notificationContent)
                    .isRead(false)
                    .build();
            
            log.info("About to save notification with type: {}", notification.getType());
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Created house deleted notification with ID: {} for host {} - house: {}", savedNotification.getId(), hostId, houseTitle);
            
            // Gửi email thông báo
            try {
                log.info("Sending email to host {} at email: {}", hostId, host.getEmail());
                emailService.sendHouseDeletedEmail(host.getEmail(), houseTitle);
                log.info("Sent house deleted email to host {} - email: {}", hostId, host.getEmail());
            } catch (Exception e) {
                log.error("Failed to send house deleted email to host {}: {}", hostId, e.getMessage(), e);
            }
            
        } catch (Exception e) {
            log.error("Failed to create house deleted notification for host {}: {}", hostId, e.getMessage(), e);
            // Không throw e để tránh rollback transaction chính
        }
    }

    @Override
    @Transactional
    public void createRentalBookedNotification(Long hostId, String userName, String houseName, Long rentalId, Long houseId) {
        try {
            log.info("Creating rental booked notification - hostId: {}, userName: {}, houseName: {}, rentalId: {}", 
                    hostId, userName, houseName, rentalId);
            
            User host = findUserByIdOrThrow(hostId);
            
            String notificationContent;
            try {
                String safeUserName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : "Khách hàng";
                String safeHouseName = (houseName != null && !houseName.trim().isEmpty()) ? houseName.trim() : "Nhà #" + houseId;
                
                notificationContent = messageSource.getMessage(
                    "notification.rental.booked", 
                    new Object[]{safeUserName, safeHouseName}, 
                    Locale.getDefault()
                );
            } catch (Exception e) {
                log.error("Failed to get message from messageSource: {}", e.getMessage());
                String safeUserName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : "Khách hàng";
                String safeHouseName = (houseName != null && !houseName.trim().isEmpty()) ? houseName.trim() : "Nhà #" + houseId;
                notificationContent = safeUserName + " đã đặt thuê " + safeHouseName + " vào ngày " + 
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
            
            Notification notification = Notification.builder()
                    .receiver(host)
                    .type(Notification.Type.RENTAL_BOOKED)
                    .content(notificationContent)
                    .isRead(false)
                    .house(houseRepository.findById(houseId).orElse(null))
                    .build();
            
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Created rental booked notification with ID: {} for host {} - user: {}, house: {}", 
                    savedNotification.getId(), hostId, userName, houseName);
            
        } catch (Exception e) {
            log.error("Failed to create rental booked notification for host {}: {}", hostId, e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void createRentalCanceledNotification(Long hostId, String userName, String houseName, Long rentalId, Long houseId) {
        try {
            log.info("Creating rental canceled notification - hostId: {}, userName: {}, houseName: {}, rentalId: {}", 
                    hostId, userName, houseName, rentalId);
            
            User host = findUserByIdOrThrow(hostId);
            
            String notificationContent;
            try {
                String safeUserName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : "Khách hàng";
                String safeHouseName = (houseName != null && !houseName.trim().isEmpty()) ? houseName.trim() : "Nhà #" + houseId;
                
                notificationContent = messageSource.getMessage(
                    "notification.rental.canceled", 
                    new Object[]{safeUserName, safeHouseName}, 
                    Locale.getDefault()
                );
            } catch (Exception e) {
                log.error("Failed to get message from messageSource: {}", e.getMessage());
                String safeUserName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : "Khách hàng";
                String safeHouseName = (houseName != null && !houseName.trim().isEmpty()) ? houseName.trim() : "Nhà #" + houseId;
                notificationContent = safeUserName + " đã hủy thuê " + safeHouseName + " vào ngày " + 
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
            
            Notification notification = Notification.builder()
                    .receiver(host)
                    .type(Notification.Type.RENTAL_CANCELED)
                    .content(notificationContent)
                    .isRead(false)
                    .house(houseRepository.findById(houseId).orElse(null))
                    .build();
            
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Created rental canceled notification with ID: {} for host {} - user: {}, house: {}", 
                    savedNotification.getId(), hostId, userName, houseName);
            
        } catch (Exception e) {
            log.error("Failed to create rental canceled notification for host {}: {}", hostId, e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void createReviewOneStarNotification(Long hostId, String userName, String houseName, Long reviewId, Long houseId) {
        try {
            log.info("Creating review one star notification - hostId: {}, userName: {}, houseName: {}, reviewId: {}", 
                    hostId, userName, houseName, reviewId);
            
            User host = findUserByIdOrThrow(hostId);
            
            String notificationContent;
            try {
                String safeUserName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : "Khách hàng";
                String safeHouseName = (houseName != null && !houseName.trim().isEmpty()) ? houseName.trim() : "Nhà #" + houseId;
                
                notificationContent = messageSource.getMessage(
                    "notification.review.one.star", 
                    new Object[]{safeUserName, safeHouseName}, 
                    Locale.getDefault()
                );
            } catch (Exception e) {
                log.error("Failed to get message from messageSource: {}", e.getMessage());
                String safeUserName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : "Khách hàng";
                String safeHouseName = (houseName != null && !houseName.trim().isEmpty()) ? houseName.trim() : "Nhà #" + houseId;
                notificationContent = safeUserName + " đã nhận xét " + safeHouseName + " vào ngày " + 
                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
            
            Notification notification = Notification.builder()
                    .receiver(host)
                    .type(Notification.Type.REVIEW_ONE_STAR)
                    .content(notificationContent)
                    .isRead(false)
                    .house(houseRepository.findById(houseId).orElse(null))
                    .review(reviewRepository.findById(reviewId).orElse(null))
                    .build();
            
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Created review one star notification with ID: {} for host {} - user: {}, house: {}", 
                    savedNotification.getId(), hostId, userName, houseName);
            
        } catch (Exception e) {
            log.error("Failed to create review one star notification for host {}: {}", hostId, e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void createRentalRequestNotification(Long hostId, String userName, String houseName, Long rentalId, Long houseId) {
        try {
            log.info("Creating rental request notification - hostId: {}, userName: {}, houseName: {}, rentalId: {}", 
                    hostId, userName, houseName, rentalId);
            
            User host = findUserByIdOrThrow(hostId);
            
            String notificationContent;
            try {
                String safeUserName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : "Khách hàng";
                String safeHouseName = (houseName != null && !houseName.trim().isEmpty()) ? houseName.trim() : "Nhà #" + houseId;
                
                notificationContent = messageSource.getMessage(
                    "notification.rental.request", 
                    new Object[]{safeUserName, safeHouseName}, 
                    Locale.getDefault()
                );
            } catch (Exception e) {
                log.error("Failed to get message from messageSource: {}", e.getMessage());
                String safeUserName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : "Khách hàng";
                String safeHouseName = (houseName != null && !houseName.trim().isEmpty()) ? houseName.trim() : "Nhà #" + houseId;
                notificationContent = "Bạn có một đơn thuê mới từ " + safeUserName + " cho nhà " + safeHouseName;
            }
            
            Notification notification = Notification.builder()
                    .receiver(host)
                    .type(Notification.Type.RENTAL_REQUEST)
                    .content(notificationContent)
                    .isRead(false)
                    .house(houseRepository.findById(houseId).orElse(null))
                    .build();
            
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Created rental request notification with ID: {} for host {} - user: {}, house: {}", 
                    savedNotification.getId(), hostId, userName, houseName);
            
        } catch (Exception e) {
            log.error("Failed to create rental request notification for host {}: {}", hostId, e.getMessage(), e);
        }
    }

    private NotificationDTO toDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .receiverId(n.getReceiver().getId())
                .type(n.getType())
                .content(n.getContent())
                .isRead(n.getIsRead())
                .houseId(n.getHouse() != null ? n.getHouse().getId() : null)
                .reviewId(n.getReview() != null ? n.getReview().getId() : null)
                .createdAt(n.getCreatedAt())
                .build();
    }
}