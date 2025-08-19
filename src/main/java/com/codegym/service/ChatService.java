package com.codegym.service;

import com.codegym.dto.request.SendMessageRequest;
import com.codegym.dto.response.ConversationDTO;
import com.codegym.dto.response.MessageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {
    
    // Conversation methods
    List<ConversationDTO> getUserConversations(Long userId);
    ConversationDTO getConversationById(Long conversationId, Long userId);
    ConversationDTO createOrGetConversation(Long userId, Long hostId, Long houseId);
    
    // Message methods
    MessageDTO sendMessage(SendMessageRequest request, Long senderId);
    Page<MessageDTO> getConversationMessages(Long conversationId, Long userId, Pageable pageable);
    void markMessagesAsRead(Long conversationId, Long userId);
    
    // Notification methods
    Long getUnreadMessageCount(Long userId);
    Long getUnreadMessageCountForConversation(Long conversationId, Long userId);
    
    /**
     * Lấy user ID theo email
     */
    Long getUserIdByEmail(String email);
} 