package com.cy.person_blog.service;

import com.cy.person_blog.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    boolean register(User user, String confirmPassword);

    User findById(Integer id);

    String updateProfile(Integer userId,
                         String nickname,
                         String bio,
                         MultipartFile avatar) throws Exception;

    void changePassword(Integer userId, String newEncoded);
    Optional<User> findByEmail(String email);
    void save(User user);
    Optional<User> authenticate(String email, String rawPassword);
}