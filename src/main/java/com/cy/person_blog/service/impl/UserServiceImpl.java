package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.User;
import com.cy.person_blog.repository.UserRepository;
import com.cy.person_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Value("${app.avatar.upload-dir}")
    private String avatarUploadDir;

    @Autowired private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean register(User user, String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) return false;
        if (userRepository.findByEmail(user.getEmail()).isPresent()) return false;
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setNickname(user.getEmail().split("@")[0]);
        user.setStatus("ACTIVE");
        user.setAvatar_url("default-avatar.png");
        userRepository.save(user);
        return true;
    }

    @Override
    public Optional<User> authenticate(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("找不到用户 ID=" + id));
    }

    @Override
    public String updateProfile(Integer userId,
                                String nickname,
                                String bio,
                                MultipartFile avatar) throws Exception {
        User user = findById(userId);
        user.setNickname(nickname);
        user.setBio(bio);

        if (avatar != null && !avatar.isEmpty()) {
            String ext = StringUtils.getFilenameExtension(avatar.getOriginalFilename());
            String filename = UUID.randomUUID() + "." + ext;

            String projectDir = System.getProperty("user.dir");
            Path uploadPath = Paths.get(projectDir, avatarUploadDir);

            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            avatar.transferTo(uploadPath.resolve(filename).toFile());

            user.setAvatar_url(filename);
        }

        userRepository.save(user);
        return nickname;
    }



    @Override
    public void changePassword(Integer userId, String encodedNewPassword) {
        User user = findById(userId);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
