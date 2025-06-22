package com.cy.person_blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    @Value("${app.upload.image-dir}")
    private String uploadDir;

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadImage(
            @RequestParam("file") MultipartFile file, HttpServletRequest request)
            throws IOException {
        Path imageDir = Paths.get(System.getProperty("user.dir"), uploadDir);
        if (Files.notExists(imageDir)) {
            Files.createDirectories(imageDir);
        }

        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = original.contains(".") ? original.substring(original.lastIndexOf(".")) : "";
        String filename = UUID.randomUUID() + ext;

        Path target = imageDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        String url = request.getContextPath() + "/" + uploadDir + filename;
        return Map.of("location", url);
    }
}