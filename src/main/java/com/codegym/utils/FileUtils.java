package com.codegym.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUtils {

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp"
    );

    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp", "pdf"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public static boolean isValidImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension == null) {
            return false;
        }

        return ALLOWED_IMAGE_TYPES.contains(extension.toLowerCase());
    }

    public static boolean isValidDocument(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return false;
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension == null) {
            return false;
        }

        return ALLOWED_DOCUMENT_TYPES.contains(extension.toLowerCase());
    }

    public static String getFileExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static String getFileNameWithoutExtension(String filename) {
        return FilenameUtils.getBaseName(filename);
    }

    public static boolean isFileSizeAcceptable(MultipartFile file) {
        return file != null && file.getSize() <= MAX_FILE_SIZE;
    }

    public static long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }

    public static long getMaxFileSizeMB() {
        return MAX_FILE_SIZE / (1024 * 1024);
    }

    public static List<String> getAllowedImageTypes() {
        return ALLOWED_IMAGE_TYPES;
    }
}
