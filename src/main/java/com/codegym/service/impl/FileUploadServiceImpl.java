package com.codegym.service.impl;

import com.codegym.dto.response.FileUploadResponse;
import com.codegym.exception.AppException;
import com.codegym.service.FileUploadService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of FileUploadService for handling file upload operations.
 * Provides image processing, validation, and storage management.
 * 
 * @author CodeGym Team
 * @version 1.0
 * @since 2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.allowed-types}")
    private String allowedTypes;

    @Value("${file.upload.max-size}")
    private long maxFileSize;

    @Value("${file.upload.avatar.width}")
    private int avatarWidth;

    @Value("${file.upload.avatar.height}")
    private int avatarHeight;

    @Value("${file.upload.house-image.width}")
    private int houseImageWidth;

    @Value("${file.upload.house-image.height}")
    private int houseImageHeight;

    @Value("${file.upload.thumbnail.width}")
    private int thumbnailWidth;

    @Value("${file.upload.thumbnail.height}")
    private int thumbnailHeight;

    @Value("${server.port}")
    private String serverPort;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, String uploadType) {
        try {
            // Validate file
            if (!isValidFile(file)) {
                throw new AppException(StatusCode.INVALID_FILE_TYPE);
            }

            // Create upload directory if it doesn't exist
            Path uploadDir = Paths.get(uploadPath, uploadType);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Generate unique filename
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String storedFilename = generateUniqueFilename(fileExtension);

            // Create file path
            Path filePath = uploadDir.resolve(storedFilename);

            // Process and save image based on upload type
            processAndSaveImage(file, filePath, uploadType);

            // Generate file URL
            String fileUrl = generateFileUrl(uploadType, storedFilename);

            log.info("File uploaded successfully: {} -> {}", originalFilename, fileUrl);

            return FileUploadResponse.builder()
                    .fileUrl(fileUrl)
                    .originalFilename(originalFilename)
                    .storedFilename(storedFilename)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .uploadType(uploadType)
                    .uploadedAt(LocalDateTime.now())
                    .message("File uploaded successfully")
                    .build();

        } catch (IOException e) {
            log.error("Error uploading file: {}", file.getOriginalFilename(), e);
            throw new AppException(StatusCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    public List<FileUploadResponse> uploadMultipleFiles(List<MultipartFile> files, String uploadType) {
        List<FileUploadResponse> responses = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                responses.add(uploadFile(file, uploadType));
            }
        }
        
        return responses;
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            // Extract filename from URL
            String filename = extractFilenameFromUrl(fileUrl);
            if (filename == null) {
                return false;
            }

            // Determine upload type from URL
            String uploadType = extractUploadTypeFromUrl(fileUrl);
            Path filePath = Paths.get(uploadPath, uploadType, filename);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("File deleted successfully: {}", filename);
                return true;
            }

            return false;
        } catch (IOException e) {
            log.error("Error deleting file: {}", fileUrl, e);
            return false;
        }
    }

    @Override
    public String getFileUrl(String filename) {
        return generateFileUrl("general", filename);
    }

    @Override
    public boolean isValidFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Check file size
        if (file.getSize() > maxFileSize) {
            return false;
        }

        // Check file type
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (fileExtension == null) {
            return false;
        }

        List<String> allowedExtensions = Arrays.asList(allowedTypes.split(","));
        return allowedExtensions.contains(fileExtension.toLowerCase());
    }

    /**
     * Process and save image with appropriate resizing based on upload type.
     */
    private void processAndSaveImage(MultipartFile file, Path filePath, String uploadType) throws IOException {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        
        // Handle PDF files (no image processing)
        if ("pdf".equalsIgnoreCase(fileExtension)) {
            Files.copy(file.getInputStream(), filePath);
            return;
        }
        
        switch (uploadType.toLowerCase()) {
            case "avatar":
                Thumbnails.of(file.getInputStream())
                        .size(avatarWidth, avatarHeight)
                        .keepAspectRatio(true)
                        .toFile(filePath.toFile());
                break;
                
            case "house-image":
                Thumbnails.of(file.getInputStream())
                        .size(houseImageWidth, houseImageHeight)
                        .keepAspectRatio(true)
                        .toFile(filePath.toFile());
                break;
                
            case "thumbnail":
                Thumbnails.of(file.getInputStream())
                        .size(thumbnailWidth, thumbnailHeight)
                        .keepAspectRatio(true)
                        .toFile(filePath.toFile());
                break;
                
            case "proof-of-ownership":
                // For proof of ownership, save original file without resizing
                Files.copy(file.getInputStream(), filePath);
                break;
                
            default:
                // Save original file without processing
                Files.copy(file.getInputStream(), filePath);
                break;
        }
    }

    /**
     * Generate a unique filename with UUID.
     */
    private String generateUniqueFilename(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }

    /**
     * Generate file URL for accessing the uploaded file.
     */
    private String generateFileUrl(String uploadType, String filename) {
        return String.format("http://localhost:%s/api/files/%s/%s", serverPort, uploadType, filename);
    }

    /**
     * Extract filename from file URL.
     */
    private String extractFilenameFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }
        
        String[] parts = fileUrl.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : null;
    }

    /**
     * Extract upload type from file URL.
     */
    private String extractUploadTypeFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return "general";
        }
        
        String[] parts = fileUrl.split("/");
        if (parts.length > 2) {
            return parts[parts.length - 2];
        }
        
        return "general";
    }
}
