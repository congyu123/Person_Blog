package com.cy.person_blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.avatar.upload-dir}")
    private String avatarUploadDir; // now "uploads/avatars"

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectDir = System.getProperty("user.dir");
        // 拼出绝对路径并保证末尾有斜杠
        String fullPath = Paths.get(projectDir, avatarUploadDir).toUri().toString() + "/";

        registry.addResourceHandler("/uploads/avatars/**")
                .addResourceLocations(fullPath);
    }
}
