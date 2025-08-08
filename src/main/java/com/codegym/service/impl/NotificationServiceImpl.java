package com.codegym.service.impl;

import com.codegym.dto.response.NotificationDTO;
import com.codegym.entity.Notification;
import com.codegym.entity.User;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.NotificationRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.NotificationService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

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