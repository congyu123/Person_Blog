package com.cy.person_blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.avatar.upload-dir}")
    private String avatarUploadDir;

    @Value("${app.upload.image-dir}")
    private String imageUploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectDir = System.getProperty("user.dir");

        String avatarsLocation = "file:" +
                Paths.get(projectDir, avatarUploadDir).toString() + "/";

        String imagesLocation = "file:" +
                Paths.get(projectDir, imageUploadDir).toString() + "/";

        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations(avatarsLocation);

        registry.addResourceHandler("/uploads/images/**")
                .addResourceLocations(imagesLocation);
    }
}