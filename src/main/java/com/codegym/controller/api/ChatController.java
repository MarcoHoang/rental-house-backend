package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.request.SendMessageRequest;
import com.codegym.dto.response.ConversationDTO;
import com.codegym.dto.response.MessageDTO;
import com.codegym.exception.AppException;
import com.codegym.service.ChatService;
import com.codegym.utils.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final MessageSource messageSource;

    @GetMapping("/conversations")
    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<List<ConversationDTO>>> getUserConversations(Locale locale) {
        try {
            Long userId = getCurrentUserId();
            log.info("Getting conversations for user: {}", userId);
            
            List<ConversationDTO> conversations = chatService.getUserConversations(userId);
            return ResponseEntity.ok(ApiResponse.success(conversations, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
        } catch (AppException e) {
            log.error("AppException in getUserConversations: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getStatusCode().getCode(), e.getMessage(), messageSource, locale));
        } catch (Exception e) {
            log.error("Unexpected error in getUserConversations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                        "Error getting conversations", messageSource, locale));
        }
    }

    @GetMapping("/conversations/{conversationId}")
    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<ConversationDTO>> getConversationById(@PathVariable Long conversationId, Locale locale) {
        try {
            Long userId = getCurrentUserId();
            log.info("Getting conversation: {} for user: {}", conversationId, userId);
            
            ConversationDTO conversation = chatService.getConversationById(conversationId, userId);
            return ResponseEntity.ok(ApiResponse.success(conversation, StatusCode.GET_DETAIL_SUCCESS, messageSource, locale));
        } catch (AppException e) {
            log.error("AppException in getConversationById: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getStatusCode().getCode(), e.getMessage(), messageSource, locale));
        } catch (Exception e) {
            log.error("Unexpected error in getConversationById", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                        "Error getting conversation", messageSource, locale));
        }
    }

    @PostMapping("/conversations")
    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<ConversationDTO>> createOrGetConversation(
            @RequestBody Map<String, Object> request, 
            Locale locale) {
        try {
            Long userId = getCurrentUserId();
            
            // Log the request body for debugging
            log.info("Creating/getting conversation request: {}", request);
            
            // Extract and validate parameters
            Object hostIdObj = request.get("hostId");
            Object houseIdObj = request.get("houseId");
            
            log.info("Extracted hostId: {} (type: {}), houseId: {} (type: {})", 
                    hostIdObj, hostIdObj != null ? hostIdObj.getClass().getSimpleName() : "null",
                    houseIdObj, houseIdObj != null ? houseIdObj.getClass().getSimpleName() : "null");
            
            if (hostIdObj == null || houseIdObj == null) {
                log.error("Missing required parameters: hostId={}, houseId={}", hostIdObj, houseIdObj);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(StatusCode.INVALID_INPUT.getCode(), 
                            "hostId and houseId are required", messageSource, locale));
            }
            
            // Convert to Long safely
            Long hostId;
            Long houseId;
            
            try {
                if (hostIdObj instanceof Integer) {
                    hostId = ((Integer) hostIdObj).longValue();
                } else if (hostIdObj instanceof Long) {
                    hostId = (Long) hostIdObj;
                } else if (hostIdObj instanceof String) {
                    hostId = Long.parseLong((String) hostIdObj);
                } else {
                    throw new IllegalArgumentException("Invalid hostId type: " + hostIdObj.getClass());
                }
                
                if (houseIdObj instanceof Integer) {
                    houseId = ((Integer) houseIdObj).longValue();
                } else if (houseIdObj instanceof Long) {
                    houseId = (Long) houseIdObj;
                } else if (houseIdObj instanceof String) {
                    houseId = Long.parseLong((String) houseIdObj);
                } else {
                    throw new IllegalArgumentException("Invalid houseId type: " + houseIdObj.getClass());
                }
            } catch (NumberFormatException e) {
                log.error("Invalid number format for hostId or houseId: hostId={}, houseId={}", hostIdObj, houseIdObj);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(StatusCode.INVALID_INPUT.getCode(), 
                            "hostId and houseId must be valid numbers", messageSource, locale));
            }
            
            // Validate that hostId and houseId are positive
            if (hostId <= 0 || houseId <= 0) {
                log.error("Invalid hostId or houseId values: hostId={}, houseId={}", hostId, houseId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error(StatusCode.INVALID_INPUT.getCode(), 
                            "hostId and houseId must be positive numbers", messageSource, locale));
            }
            
            log.info("Creating/getting conversation for user: {}, host: {}, house: {}", userId, hostId, houseId);
            
            ConversationDTO conversation = chatService.createOrGetConversation(userId, hostId, houseId);
            return ResponseEntity.ok(ApiResponse.success(conversation, StatusCode.SUCCESS, messageSource, locale));
        } catch (AppException e) {
            log.error("AppException in createOrGetConversation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getStatusCode().getCode(), e.getMessage(), messageSource, locale));
        } catch (Exception e) {
            log.error("Unexpected error in createOrGetConversation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                        "Error creating conversation", messageSource, locale));
        }
    }

    @PostMapping("/messages")
    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<MessageDTO>> sendMessage(@RequestBody @Valid SendMessageRequest request, Locale locale) {
        try {
            Long senderId = getCurrentUserId();
            log.info("Sending message from user: {} to user: {} for house: {}", 
                    senderId, request.getReceiverId(), request.getHouseId());
            
            MessageDTO message = chatService.sendMessage(request, senderId);
            return ResponseEntity.ok(ApiResponse.success(message, StatusCode.CREATED_SUCCESS, messageSource, locale));
        } catch (AppException e) {
            log.error("AppException in sendMessage: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getStatusCode().getCode(), e.getMessage(), messageSource, locale));
        } catch (Exception e) {
            log.error("Unexpected error in sendMessage", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                        "Error sending message", messageSource, locale));
        }
    }

    @GetMapping("/conversations/{conversationId}/messages")
    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<Page<MessageDTO>>> getConversationMessages(
            @PathVariable Long conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Locale locale) {
        try {
            Long userId = getCurrentUserId();
            log.info("Getting messages for conversation: {}, user: {}, page: {}, size: {}", 
                    conversationId, userId, page, size);
            
            Pageable pageable = PageRequest.of(page, size);
            Page<MessageDTO> messages = chatService.getConversationMessages(conversationId, userId, pageable);
            return ResponseEntity.ok(ApiResponse.success(messages, StatusCode.GET_LIST_SUCCESS, messageSource, locale));
        } catch (AppException e) {
            log.error("AppException in getConversationMessages: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getStatusCode().getCode(), e.getMessage(), messageSource, locale));
        } catch (Exception e) {
            log.error("Unexpected error in getConversationMessages", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                        "Error getting messages", messageSource, locale));
        }
    }

    @PostMapping("/conversations/{conversationId}/read")
    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<Void>> markMessagesAsRead(@PathVariable Long conversationId, Locale locale) {
        try {
            Long userId = getCurrentUserId();
            log.info("Marking messages as read for conversation: {}, user: {}", conversationId, userId);
            
            chatService.markMessagesAsRead(conversationId, userId);
            return ResponseEntity.ok(ApiResponse.success(StatusCode.UPDATED_SUCCESS, messageSource, locale));
        } catch (AppException e) {
            log.error("AppException in markMessagesAsRead: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getStatusCode().getCode(), e.getMessage(), messageSource, locale));
        } catch (Exception e) {
            log.error("Unexpected error in markMessagesAsRead", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                        "Error marking messages as read", messageSource, locale));
        }
    }

    @GetMapping("/unread-count")
    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<Long>> getUnreadMessageCount(Locale locale) {
        try {
            Long userId = getCurrentUserId();
            log.info("Getting unread message count for user: {}", userId);
            
            Long count = chatService.getUnreadMessageCount(userId);
            return ResponseEntity.ok(ApiResponse.success(count, StatusCode.SUCCESS, messageSource, locale));
        } catch (AppException e) {
            log.error("AppException in getUnreadMessageCount: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getStatusCode().getCode(), e.getMessage(), messageSource, locale));
        } catch (Exception e) {
            log.error("Unexpected error in getUnreadMessageCount", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                        "Error getting unread count", messageSource, locale));
        }
    }

    @GetMapping("/conversations/{conversationId}/unread-count")
    @PreAuthorize("hasRole('USER') or hasRole('HOST')")
    public ResponseEntity<ApiResponse<Long>> getUnreadMessageCountForConversation(
            @PathVariable Long conversationId, 
            Locale locale) {
        try {
            Long userId = getCurrentUserId();
            log.info("Getting unread message count for conversation: {}, user: {}", conversationId, userId);
            
            Long count = chatService.getUnreadMessageCountForConversation(conversationId, userId);
            return ResponseEntity.ok(ApiResponse.success(count, StatusCode.SUCCESS, messageSource, locale));
        } catch (AppException e) {
            log.error("AppException in getUnreadMessageCountForConversation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getStatusCode().getCode(), e.getMessage(), messageSource, locale));
        } catch (Exception e) {
            log.error("Unexpected error in getUnreadMessageCountForConversation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(StatusCode.INTERNAL_ERROR.getCode(), 
                        "Error getting unread count", messageSource, locale));
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("User not authenticated");
            throw new AppException(StatusCode.UNAUTHORIZED);
        }
        
        try {
            Object principal = authentication.getPrincipal();
            log.debug("Principal type: {}", principal.getClass().getSimpleName());
            
            // Handle JWT Claims
            if (principal instanceof io.jsonwebtoken.Claims) {
                io.jsonwebtoken.Claims claims = (io.jsonwebtoken.Claims) principal;
                Object idObj = claims.get("id");
                
                if (idObj != null) {
                    if (idObj instanceof Integer) {
                        return ((Integer) idObj).longValue();
                    } else if (idObj instanceof Long) {
                        return (Long) idObj;
                    } else if (idObj instanceof String) {
                        return Long.parseLong((String) idObj);
                    }
                }
            } 
            // Handle Map claims (alternative JWT format)
            else if (principal instanceof Map) {
                Map<String, Object> claims = (Map<String, Object>) principal;
                Object idObj = claims.get("id");
                
                if (idObj != null) {
                    if (idObj instanceof Integer) {
                        return ((Integer) idObj).longValue();
                    } else if (idObj instanceof Long) {
                        return (Long) idObj;
                    } else if (idObj instanceof String) {
                        return Long.parseLong((String) idObj);
                    }
                }
            }
            // Handle UserDetails (Spring Security User)
            else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                String email = authentication.getName();
                if (email != null && !email.equals("anonymousUser")) {
                    log.debug("Getting userId by email from UserDetails: {}", email);
                    return chatService.getUserIdByEmail(email);
                }
            }
            
            // Fallback: lấy từ email
            String email = authentication.getName();
            if (email != null && !email.equals("anonymousUser")) {
                log.debug("Getting userId by email: {}", email);
                return chatService.getUserIdByEmail(email);
            }
            
            log.error("User ID not found in token. Principal: {}", principal);
            throw new AppException(StatusCode.UNAUTHORIZED);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error extracting user ID from authentication", e);
            throw new AppException(StatusCode.UNAUTHORIZED);
        }
    }
} 