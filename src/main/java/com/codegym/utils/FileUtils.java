package com.codegym.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for file operations and validation.
 * Provides helper methods for file handling and validation.
 * 
 * @author CodeGym Team
 * @version 1.0
 * @since 2024
 */
public class FileUtils {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp"
    );

    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp", "pdf"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    /**
     * Check if the file is a valid image.
     * 
     * @param file The file to validate
     * @return true if the file is a valid image, false otherwise
     */
    public static boolean isValidImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        // Check file extension
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension == null) {
            return false;
        }

        return ALLOWED_IMAGE_TYPES.contains(extension.toLowerCase());
    }

    /**
     * Check if the file is a valid document (image or PDF).
     * 
     * @param file The file to validate
     * @return true if the file is a valid document, false otherwise
     */
    public static boolean isValidDocument(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        // Check file extension
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension == null) {
            return false;
        }

        return ALLOWED_DOCUMENT_TYPES.contains(extension.toLowerCase());
    }

    /**
     * Get the file extension from a filename.
     * 
     * @param filename The filename
     * @return The file extension (without dot)
     */
    public static String getFileExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    /**
     * Get the file name without extension.
     * 
     * @param filename The filename
     * @return The filename without extension
     */
    public static String getFileNameWithoutExtension(String filename) {
        return FilenameUtils.getBaseName(filename);
    }

    /**
     * Check if the file size is within the allowed limit.
     * 
     * @param file The file to check
     * @return true if the file size is acceptable, false otherwise
     */
    public static boolean isFileSizeAcceptable(MultipartFile file) {
        return file != null && file.getSize() <= MAX_FILE_SIZE;
    }

    /**
     * Get the maximum allowed file size in bytes.
     * 
     * @return The maximum file size in bytes
     */
    public static long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }

    /**
     * Get the maximum allowed file size in MB.
     * 
     * @return The maximum file size in MB
     */
    public static long getMaxFileSizeMB() {
        return MAX_FILE_SIZE / (1024 * 1024);
    }

    /**
     * Get the list of allowed image types.
     * 
     * @return List of allowed image file extensions
     */
    public static List<String> getAllowedImageTypes() {
        return ALLOWED_IMAGE_TYPES;
    }
}
