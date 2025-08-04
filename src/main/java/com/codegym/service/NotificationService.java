package com.codegym.service;

import com.codegym.dto.response.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> findByReceiverId(Long userId);
    NotificationDTO markAsRead(Long id);
    void deleteById(Long id);
    NotificationDTO create(NotificationDTO dto);
}
