package com.codegym.dto.response;

import com.codegym.entity.Notification;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;
    private Long receiverId;
    private Notification.Type type;
    private String content;
    private Boolean isRead;
    private Long houseId;
    private Long reviewId;
    private LocalDateTime createdAt;
}
