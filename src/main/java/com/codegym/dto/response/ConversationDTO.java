package com.codegym.dto.response;

import com.codegym.entity.Conversation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationDTO {
    private Long id;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Long hostId;
    private String hostName;
    private String hostAvatar;
    private Long houseId;
    private String houseTitle;
    private String houseImage;
    private String lastMessageContent;
    private LocalDateTime lastMessageAt;
    private Integer unreadCount;
    private Conversation.ConversationStatus status;
    private List<MessageDTO> messages;
    private LocalDateTime createdAt;
} 