package com.codegym.dto.request;

import com.codegym.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMessageRequest {
    
    @NotNull(message = "House ID is required")
    private Long houseId;
    
    @NotNull(message = "Receiver ID is required")
    private Long receiverId;
    
    @NotBlank(message = "Message content is required")
    private String content;
    
    @Builder.Default
    private Message.MessageType messageType = Message.MessageType.TEXT;
} 