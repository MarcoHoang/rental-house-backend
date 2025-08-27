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

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${app.base-url:http://localhost}")
    private String baseUrl;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, String uploadType) {
        try {
            if (!isValidFile(file)) {
                throw new AppException(StatusCode.INVALID_FILE_TYPE);
            }

            Path uploadDir = Paths.get(uploadPath, uploadType);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = FilenameUtils.getExtension(originalFilename);
            String storedFilename = generateUniqueFilename(fileExtension);

            Path filePath = uploadDir.resolve(storedFilename);

            processAndSaveImage(file, filePath, uploadType);

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
            String filename = extractFilenameFromUrl(fileUrl);
            if (filename == null) {
                return false;
            }

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

        if (file.getSize() > maxFileSize) {
            return false;
        }

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (fileExtension == null) {
            return false;
        }

        List<String> allowedExtensions = Arrays.asList(allowedTypes.split(","));
        return allowedExtensions.contains(fileExtension.toLowerCase());
    }

    private void processAndSaveImage(MultipartFile file, Path filePath, String uploadType) throws IOException {
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        
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
                Files.copy(file.getInputStream(), filePath);
                break;
                
            default:
                Files.copy(file.getInputStream(), filePath);
                break;
        }
    }

    private String generateUniqueFilename(String extension) {
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String generateFileUrl(String uploadType, String filename) {
        String context = contextPath.isEmpty() ? "" : contextPath;
        return String.format("%s:%s%s/api/files/%s/%s", baseUrl, serverPort, context, uploadType, filename);
    }

    private String extractFilenameFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }
        
        String[] parts = fileUrl.split("/");
        return parts.length > 0 ? parts[parts.length - 1] : null;
    }

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
