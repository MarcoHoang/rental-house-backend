package com.codegym.dto.response;

import com.codegym.entity.Message;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private Long id;
    private Long conversationId;
    private Long senderId;
    private String senderName;
    private String senderAvatar;
    private Long receiverId;
    private String receiverName;
    private Long houseId;
    private String houseTitle;
    private String content;
    private Message.MessageType messageType;
    private Message.MessageStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private boolean isOwnMessage;
} 