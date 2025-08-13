package com.codegym.service;

import com.codegym.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Service interface for handling file upload operations.
 * Provides methods for uploading, processing, and managing image files.
 * 
 * @author CodeGym Team
 * @version 1.0
 * @since 2024
 */
public interface FileUploadService {

    /**
     * Upload a single file and return the file information.
     * 
     * @param file The file to upload
     * @param uploadType The type of upload (avatar, house-image, etc.)
     * @return FileUploadResponse containing the file URL and metadata
     */
    FileUploadResponse uploadFile(MultipartFile file, String uploadType);

    /**
     * Upload multiple files and return their information.
     * 
     * @param files List of files to upload
     * @param uploadType The type of upload
     * @return List of FileUploadResponse objects
     */
    List<FileUploadResponse> uploadMultipleFiles(List<MultipartFile> files, String uploadType);

    /**
     * Delete a file from storage.
     * 
     * @param fileUrl The URL of the file to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteFile(String fileUrl);

    /**
     * Get the file URL for a given filename.
     * 
     * @param filename The filename to get URL for
     * @return The complete URL for the file
     */
    String getFileUrl(String filename);

    /**
     * Validate if the file is acceptable for upload.
     * 
     * @param file The file to validate
     * @return true if file is valid, false otherwise
     */
    boolean isValidFile(MultipartFile file);
}



