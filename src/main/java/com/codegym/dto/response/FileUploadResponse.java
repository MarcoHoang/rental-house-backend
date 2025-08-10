package com.codegym.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Response DTO for file upload operations.
 * Contains information about the uploaded file including URL and metadata.
 * 
 * @author CodeGym Team
 * @version 1.0
 * @since 2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponse {
    
    /**
     * The URL where the uploaded file can be accessed
     */
    private String fileUrl;
    
    /**
     * The original filename
     */
    private String originalFilename;
    
    /**
     * The filename as stored on the server
     */
    private String storedFilename;
    
    /**
     * The file size in bytes
     */
    private Long fileSize;
    
    /**
     * The MIME type of the file
     */
    private String contentType;
    
    /**
     * The type of upload (avatar, house-image, etc.)
     */
    private String uploadType;
    
    /**
     * Timestamp when the file was uploaded
     */
    private LocalDateTime uploadedAt;
    
    /**
     * Success message
     */
    private String message;
}
