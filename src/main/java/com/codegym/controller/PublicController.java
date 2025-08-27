package com.codegym.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.codegym.repository.RentalRepository;
import com.codegym.entity.Rental;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class PublicController {

    @GetMapping("/files/{uploadType}/{filename:.+}")
    public ResponseEntity<Resource> servePublicFile(
            @PathVariable String uploadType,
            @PathVariable String filename) {

        try {
            log.info("Serving public file: {}/{}", uploadType, filename);
            Path filePath = Paths.get("uploads", uploadType, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Determine content type based on file extension
                String contentType = determineContentType(filename);
                log.info("File found and readable: {}/{}", uploadType, filename);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                log.warn("File not found or not readable: {}/{}", uploadType, filename);
                return ResponseEntity.notFound().build();
            }

        } catch (MalformedURLException e) {
            log.error("Error serving public file: {}/{}", uploadType, filename, e);
            return ResponseEntity.notFound().build();
        }
    }

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
    
    // Test endpoint để kiểm tra notification
    @GetMapping("/test-notification")
    public ResponseEntity<Map<String, Object>> testNotification() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Test notification endpoint");
        result.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(result);
    }
}
