package com.codegym.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/static")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
@Order(1) // Higher priority to bypass JWT filter
public class StaticFileController {

    @GetMapping("/uploads/{uploadType}/{filename:.+}")
    public ResponseEntity<Resource> serveStaticFile(
            @PathVariable String uploadType,
            @PathVariable String filename) {

        try {
            log.info("Serving static file: {}/{}", uploadType, filename);
            Path filePath = Paths.get("uploads", uploadType, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Determine content type based on file extension
                String contentType = determineContentType(filename);
                log.info("Static file found and readable: {}/{}", uploadType, filename);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                log.warn("Static file not found or not readable: {}/{}", uploadType, filename);
                return ResponseEntity.notFound().build();
            }

        } catch (MalformedURLException e) {
            log.error("Error serving static file: {}/{}", uploadType, filename, e);
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
}
