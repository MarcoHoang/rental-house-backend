package com.codegym.service.impl;

import com.codegym.dto.response.NotificationDTO;
import com.codegym.entity.Notification;
import com.codegym.entity.User;
import com.codegym.repository.NotificationRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<NotificationDTO> findByReceiverId(Long userId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public NotificationDTO markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notification.setIsRead(true);
        return toDTO(notificationRepository.save(notification));
    }

    @Override
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public NotificationDTO create(NotificationDTO dto) {
        User user = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Notification notification = Notification.builder()
                .receiver(user)
                .type(dto.getType())
                .content(dto.getContent())
                .isRead(false)
                .build();
        return toDTO(notificationRepository.save(notification));
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