package com.codegym.service.impl;

import com.codegym.dto.response.NotificationDTO;
import com.codegym.entity.Notification;
import com.codegym.entity.User;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.NotificationRepository;
import com.codegym.repository.UserRepository;
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

    private NotificationDTO toDTO(Notification n) {
        return NotificationDTO.builder()
                .id(n.getId())
                .receiverId(n.getReceiver().getId())
                .type(n.getType())
                .content(n.getContent())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}