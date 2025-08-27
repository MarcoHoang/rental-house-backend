package com.codegym.service;

import com.codegym.dto.response.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    FileUploadResponse uploadFile(MultipartFile file, String uploadType);

    List<FileUploadResponse> uploadMultipleFiles(List<MultipartFile> files, String uploadType);

    boolean deleteFile(String fileUrl);

    String getFileUrl(String filename);

    boolean isValidFile(MultipartFile file);
}




