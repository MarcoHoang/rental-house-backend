package com.codegym.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponse {
    
    private String fileUrl;

    private String originalFilename;

    private String storedFilename;

    private Long fileSize;

    private String contentType;

    private String uploadType;

    private LocalDateTime uploadedAt;

    private String message;
}




