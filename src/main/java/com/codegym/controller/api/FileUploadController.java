package com.codegym.controller.api;

import com.codegym.dto.ApiResponse;
import com.codegym.dto.response.FileUploadResponse;
import com.codegym.service.FileUploadService;
import com.codegym.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * REST controller for handling file upload and download operations.
 * Provides endpoints for uploading images and serving static files.
 *
 * @author CodeGym Team
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final MessageSource messageSource;

    /**
     * Upload a single file.
     *
     * @param file The file to upload
     * @param uploadType The type of upload (avatar, house-image, etc.)
     * @param locale The locale for internationalized messages
     * @return ResponseEntity containing the upload response
     */
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploadType") String uploadType,
            Locale locale) {

        FileUploadResponse response = fileUploadService.uploadFile(file, uploadType);

        return ResponseEntity.ok(
                ApiResponse.success(response, StatusCode.FILE_UPLOAD_SUCCESS, messageSource, locale)
        );
    }

    /**
     * Upload multiple files.
     *
     * @param files List of files to upload
     * @param uploadType The type of upload
     * @param locale The locale for internationalized messages
     * @return ResponseEntity containing the upload responses
     */
    @PostMapping("/upload/multiple")
    public ResponseEntity<ApiResponse<List<FileUploadResponse>>> uploadMultipleFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("uploadType") String uploadType,
            Locale locale) {

        List<FileUploadResponse> responses = fileUploadService.uploadMultipleFiles(files, uploadType);

        return ResponseEntity.ok(
                ApiResponse.success(responses, StatusCode.FILE_UPLOAD_SUCCESS, messageSource, locale)
        );
    }

    /**
     * Delete a file.
     *
     * @param fileUrl The URL of the file to delete
     * @param locale The locale for internationalized messages
     * @return ResponseEntity indicating success or failure
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @RequestParam("fileUrl") String fileUrl,
            Locale locale) {

        boolean deleted = fileUploadService.deleteFile(fileUrl);

        if (deleted) {
            return ResponseEntity.ok(
                    ApiResponse.success(StatusCode.FILE_DELETE_SUCCESS, messageSource, locale)
            );
        } else {
            String errorMessage = messageSource.getMessage(StatusCode.FILE_NOT_FOUND.getMessageKey(), null, locale);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse<>(StatusCode.FILE_NOT_FOUND.getCode(), errorMessage)
            );
        }
    }

    /**
     * Serve a file by its upload type and filename.
     *
     * @param uploadType The type of upload (avatar, house-image, etc.)
     * @param filename The filename to serve
     * @return ResponseEntity containing the file resource
     */
    @GetMapping("/{uploadType}/{filename:.+}")
    public ResponseEntity<Resource> serveFile(
            @PathVariable String uploadType,
            @PathVariable String filename) {

        try {
            Path filePath = Paths.get("uploads", uploadType, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Determine content type based on file extension
                String contentType = determineContentType(filename);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (MalformedURLException e) {
            log.error("Error serving file: {}/{}", uploadType, filename, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Determine the content type based on file extension.
     *
     * @param filename The filename
     * @return The appropriate content type
     */
    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            case "pdf" -> "application/pdf";
            default -> "application/octet-stream";
        };
    }

    /**
     * Upload user avatar.
     *
     * @param file The avatar image file
     * @param locale The locale for internationalized messages
     * @return ResponseEntity containing the upload response
     */
    @PostMapping("/upload/avatar")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            Locale locale) {

        FileUploadResponse response = fileUploadService.uploadFile(file, "avatar");

        return ResponseEntity.ok(
                ApiResponse.success(response, StatusCode.FILE_UPLOAD_SUCCESS, messageSource, locale)
        );
    }

    /**
     * Upload house images.
     *
     * @param files List of house image files
     * @param locale The locale for internationalized messages
     * @return ResponseEntity containing the upload responses
     */
    @PostMapping("/upload/house-images")
    public ResponseEntity<ApiResponse<List<FileUploadResponse>>> uploadHouseImages(
            @RequestParam("files") List<MultipartFile> files,
            Locale locale) {

        List<FileUploadResponse> responses = fileUploadService.uploadMultipleFiles(files, "house-image");

        return ResponseEntity.ok(
                ApiResponse.success(responses, StatusCode.FILE_UPLOAD_SUCCESS, messageSource, locale)
        );
    }

    /**
     * Upload proof of ownership document for house renter.
     *
     * @param file The proof of ownership document
     * @param locale The locale for internationalized messages
     * @return ResponseEntity containing the upload response
     */
    @PostMapping("/upload/proof-of-ownership")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadProofOfOwnership(
            @RequestParam("file") MultipartFile file,
            Locale locale) {

        FileUploadResponse response = fileUploadService.uploadFile(file, "proof-of-ownership");

        return ResponseEntity.ok(
                ApiResponse.success(response, StatusCode.FILE_UPLOAD_SUCCESS, messageSource, locale)
        );
    }
}
