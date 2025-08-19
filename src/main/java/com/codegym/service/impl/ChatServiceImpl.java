package com.codegym.service.impl;

import com.codegym.dto.request.SendMessageRequest;
import com.codegym.dto.response.ConversationDTO;
import com.codegym.dto.response.MessageDTO;
import com.codegym.entity.Conversation;
import com.codegym.entity.House;
import com.codegym.entity.Message;
import com.codegym.entity.User;
import com.codegym.exception.AppException;
import com.codegym.exception.ResourceNotFoundException;
import com.codegym.repository.ConversationRepository;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.MessageRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.ChatService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    @Override
    public List<ConversationDTO> getUserConversations(Long userId) {
        try {
            List<Conversation> conversations = conversationRepository.findByUserIdOrderByLastMessageAtDesc(userId);
            return conversations.stream()
                    .map(this::convertToConversationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting user conversations for userId: {}", userId, e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    @Override
    public ConversationDTO getConversationById(Long conversationId, Long userId) {
        try {
            Optional<Conversation> conversation = conversationRepository.findByIdAndUserId(conversationId, userId);
            if (conversation.isPresent()) {
                return convertToConversationDTO(conversation.get());
            }
            throw new ResourceNotFoundException(StatusCode.CONVERSATION_NOT_FOUND, conversationId);
        } catch (Exception e) {
            log.error("Error getting conversation by id: {} for userId: {}", conversationId, userId, e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    @Override
    public ConversationDTO createOrGetConversation(Long userId, Long hostId, Long houseId) {
        try {
            // Validate input parameters
            if (userId == null || hostId == null || houseId == null) {
                log.error("Invalid input parameters: userId={}, hostId={}, houseId={}", userId, hostId, houseId);
                throw new AppException(StatusCode.INVALID_INPUT);
            }

            // Check if users and house exist
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        log.error("User not found with id: {}", userId);
                        return new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, userId);
                    });
            User host = userRepository.findById(hostId)
                    .orElseThrow(() -> {
                        log.error("Host not found with id: {}", hostId);
                        return new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, hostId);
                    });
            House house = houseRepository.findById(houseId)
                    .orElseThrow(() -> {
                        log.error("House not found with id: {}", houseId);
                        return new ResourceNotFoundException(StatusCode.HOUSE_NOT_FOUND, houseId);
                    });

            // Prevent user from chatting with themselves
            if (userId.equals(hostId)) {
                log.warn("User {} attempted to chat with themselves", userId);
                throw new AppException(StatusCode.CONVERSATION_SELF_CHAT);
            }

            // Check if conversation already exists (both directions)
            Optional<Conversation> existingConversation = conversationRepository
                    .findByUserIdAndHostIdAndHouseId(userId, hostId, houseId);

            if (existingConversation.isPresent()) {
                log.info("Found existing conversation: {} between user: {} and host: {} for house: {}", 
                        existingConversation.get().getId(), userId, hostId, houseId);
                return convertToConversationDTO(existingConversation.get());
            }

            // Create new conversation
            Conversation conversation = Conversation.builder()
                    .user(user)
                    .host(host)
                    .house(house)
                    .status(Conversation.ConversationStatus.ACTIVE)
                    .unreadCountUser(0)
                    .unreadCountHost(0)
                    .lastMessageAt(LocalDateTime.now())
                    .build();

            Conversation savedConversation = conversationRepository.save(conversation);
            log.info("Created new conversation: {} between user: {} and host: {} for house: {}", 
                    savedConversation.getId(), userId, hostId, houseId);
            
            return convertToConversationDTO(savedConversation);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating/getting conversation for userId: {}, hostId: {}, houseId: {}", 
                    userId, hostId, houseId, e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    @Override
    public MessageDTO sendMessage(SendMessageRequest request, Long senderId) {
        try {
            // Validate input parameters
            if (request == null || senderId == null) {
                throw new AppException(StatusCode.INVALID_INPUT);
            }

            // Validate request fields
            if (request.getReceiverId() == null || request.getHouseId() == null || 
                request.getContent() == null || request.getContent().trim().isEmpty()) {
                throw new AppException(StatusCode.INVALID_INPUT);
            }

            // Get sender, receiver and house
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, senderId));
            User receiver = userRepository.findById(request.getReceiverId())
                    .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, request.getReceiverId()));
            House house = houseRepository.findById(request.getHouseId())
                    .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOUSE_NOT_FOUND, request.getHouseId()));

            // Get or create conversation
            Conversation conversation = getOrCreateConversation(senderId, request.getReceiverId(), request.getHouseId());

            // Create message
            Message message = Message.builder()
                    .conversation(conversation)
                    .sender(sender)
                    .receiver(receiver)
                    .house(house)
                    .content(request.getContent().trim())
                    .messageType(request.getMessageType() != null ? request.getMessageType() : Message.MessageType.TEXT)
                    .status(Message.MessageStatus.SENT)
                    .build();

            Message savedMessage = messageRepository.save(message);

            // Update conversation
            conversation.setLastMessageContent(request.getContent().trim());
            conversation.setLastMessageAt(LocalDateTime.now());
            
            // Update unread count
            if (senderId.equals(conversation.getUser().getId())) {
                conversation.setUnreadCountHost(conversation.getUnreadCountHost() + 1);
            } else {
                conversation.setUnreadCountUser(conversation.getUnreadCountUser() + 1);
            }
            
            conversationRepository.save(conversation);

            log.info("Message sent successfully: {} from user: {} to user: {} in conversation: {}", 
                    savedMessage.getId(), senderId, request.getReceiverId(), conversation.getId());

            return convertToMessageDTO(savedMessage, senderId);
        } catch (Exception e) {
            log.error("Error sending message from senderId: {} to receiverId: {} for houseId: {}", 
                    senderId, request.getReceiverId(), request.getHouseId(), e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    private Conversation getOrCreateConversation(Long userId, Long hostId, Long houseId) {
        // Try to find existing conversation (both directions)
        Optional<Conversation> existingConversation = conversationRepository
                .findByUserIdAndHostIdAndHouseId(userId, hostId, houseId);

        if (existingConversation.isPresent()) {
            return existingConversation.get();
        }

        // Create new conversation
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, userId));
        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, hostId));
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ResourceNotFoundException(StatusCode.HOUSE_NOT_FOUND, houseId));

        Conversation conversation = Conversation.builder()
                .user(user)
                .host(host)
                .house(house)
                .status(Conversation.ConversationStatus.ACTIVE)
                .unreadCountUser(0)
                .unreadCountHost(0)
                .lastMessageAt(LocalDateTime.now())
                .build();

        return conversationRepository.save(conversation);
    }

    @Override
    public Page<MessageDTO> getConversationMessages(Long conversationId, Long userId, Pageable pageable) {
        try {
            // Verify user has access to conversation
            conversationRepository.findByIdAndUserId(conversationId, userId)
                    .orElseThrow(() -> new ResourceNotFoundException(StatusCode.CONVERSATION_NOT_FOUND, conversationId));

            Page<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId, pageable);
            return messages.map(message -> convertToMessageDTO(message, userId));
        } catch (Exception e) {
            log.error("Error getting messages for conversationId: {} and userId: {}", conversationId, userId, e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    @Override
    public void markMessagesAsRead(Long conversationId, Long userId) {
        try {
            Optional<Conversation> conversation = conversationRepository.findByIdAndUserId(conversationId, userId);
            if (conversation.isPresent()) {
                List<Message> unreadMessages = messageRepository
                        .findUnreadMessagesByConversationAndUser(conversationId, userId);
                
                unreadMessages.forEach(message -> {
                    message.setStatus(Message.MessageStatus.READ);
                    message.setReadAt(LocalDateTime.now());
                });
                
                messageRepository.saveAll(unreadMessages);

                // Reset unread count
                Conversation conv = conversation.get();
                if (userId.equals(conv.getUser().getId())) {
                    conv.setUnreadCountUser(0);
                } else {
                    conv.setUnreadCountHost(0);
                }
                conversationRepository.save(conv);
            }
        } catch (Exception e) {
            log.error("Error marking messages as read for conversationId: {} and userId: {}", conversationId, userId, e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    @Override
    public Long getUnreadMessageCount(Long userId) {
        try {
            return conversationRepository.countUnreadConversationsByUserId(userId);
        } catch (Exception e) {
            log.error("Error getting unread message count for userId: {}", userId, e);
            return 0L;
        }
    }

    @Override
    public Long getUnreadMessageCountForConversation(Long conversationId, Long userId) {
        try {
            return messageRepository.countByConversationIdAndReceiverIdAndIsReadFalse(conversationId, userId);
        } catch (Exception e) {
            log.error("Error getting unread message count for conversationId: {} and userId: {}", conversationId, userId, e);
            return 0L;
        }
    }
    
    @Override
    public Long getUserIdByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(StatusCode.USER_NOT_FOUND, 0L));
            return user.getId();
        } catch (Exception e) {
            log.error("Error getting userId by email: {}", email, e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    private ConversationDTO convertToConversationDTO(Conversation conversation) {
        try {
            return ConversationDTO.builder()
                    .id(conversation.getId())
                    .userId(conversation.getUser().getId())
                    .userName(conversation.getUser().getFullName() != null ? conversation.getUser().getFullName() : conversation.getUser().getUsername())
                    .userAvatar(conversation.getUser().getAvatarUrl())
                    .hostId(conversation.getHost().getId())
                    .hostName(conversation.getHost().getFullName() != null ? conversation.getHost().getFullName() : conversation.getHost().getUsername())
                    .hostAvatar(conversation.getHost().getAvatarUrl())
                    .houseId(conversation.getHouse().getId())
                    .houseTitle(conversation.getHouse().getTitle())
                    .houseImage(conversation.getHouse().getImages() != null && !conversation.getHouse().getImages().isEmpty() 
                            ? conversation.getHouse().getImages().get(0).getImageUrl() : null)
                    .lastMessageContent(conversation.getLastMessageContent())
                    .lastMessageAt(conversation.getLastMessageAt())
                    .unreadCount(conversation.getUnreadCountUser() + conversation.getUnreadCountHost())
                    .status(conversation.getStatus())
                    .createdAt(conversation.getCreatedAt())
                    .build();
        } catch (Exception e) {
            log.error("Error converting conversation to DTO: {}", conversation.getId(), e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }

    private MessageDTO convertToMessageDTO(Message message, Long currentUserId) {
        try {
            return MessageDTO.builder()
                    .id(message.getId())
                    .conversationId(message.getConversation().getId())
                    .senderId(message.getSender().getId())
                    .senderName(message.getSender().getFullName() != null ? message.getSender().getFullName() : message.getSender().getUsername())
                    .senderAvatar(message.getSender().getAvatarUrl())
                    .receiverId(message.getReceiver().getId())
                    .receiverName(message.getReceiver().getFullName() != null ? message.getReceiver().getFullName() : message.getReceiver().getUsername())
                    .houseId(message.getHouse().getId())
                    .houseTitle(message.getHouse().getTitle())
                    .content(message.getContent())
                    .messageType(message.getMessageType())
                    .status(message.getStatus())
                    .createdAt(message.getCreatedAt())
                    .readAt(message.getReadAt())
                    .isOwnMessage(message.getSender().getId().equals(currentUserId))
                    .build();
        } catch (Exception e) {
            log.error("Error converting message to DTO: {}", message.getId(), e);
            throw new AppException(StatusCode.INTERNAL_ERROR);
        }
    }
} 