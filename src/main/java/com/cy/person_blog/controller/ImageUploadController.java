package com.cy.person_blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    @Value("${app.upload.dir:${user.home}/uploads}")
    private String uploadDir;

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        Path imageDir = Paths.get(uploadDir, "images");
        if (!Files.exists(imageDir)) {
            Files.createDirectories(imageDir);
        }

        String original = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot > 0) ext = original.substring(dot);
        String filename = UUID.randomUUID() + ext;

        Path target = imageDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        Map<String, String> result = new HashMap<>();
        result.put("location", "/uploads/images/" + filename);
        return result;
    }
}
