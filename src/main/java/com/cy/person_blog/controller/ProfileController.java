package com.cy.person_blog.controller;

import com.cy.person_blog.entity.User;
import com.cy.person_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProfileController {

    @Autowired private UserService userService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) {
            return "redirect:/login";
        }
        User user = userService.findById(current.getId());
        model.addAttribute("user", user);

        String avatarUrl = user.getAvatar_url() != null
                ? "/uploads/avatars/" + user.getAvatar_url()
                : "/static/img/default-avatar.png";
        model.addAttribute("userAvatarUrl", avatarUrl);
        return "profile";
    }


    @PostMapping(value = "/profile/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> updateInfo(
            HttpSession session,
            @RequestParam String nickname,
            @RequestParam String bio,
            @RequestParam("avatar") MultipartFile avatar) {
        Map<String, Object> resp = new HashMap<>();
        User current = (User) session.getAttribute("currentUser");
        if (current == null) {
            resp.put("success", false);
            resp.put("error", "未登录");
            return resp;
        }
        try {
            // 调用 service 保存头像并更新用户
            String filename = userService.updateProfile(current.getId(), nickname, bio, avatar);
            // 把 session 中的 user 也更新
            User updated = userService.findById(current.getId());
            session.setAttribute("currentUser", updated);

            resp.put("success", true);
            resp.put("avatarUrl", "/uploads/avatars/" + filename);
        } catch (Exception e) {
            resp.put("success", false);
            resp.put("error", e.getMessage());
        }
        return resp;
    }


    @PostMapping("/profile/password")
    public String updatePassword(HttpSession session,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) return "redirect:/login";

        User fresh = userService.findById(current.getId());
        if (!passwordEncoder.matches(oldPassword, fresh.getPassword())) {
            model.addAttribute("errorPwd", "旧密码不正确");
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorPwd", "两次密码不一致");
        } else {
            String encoded = passwordEncoder.encode(newPassword);
            userService.changePassword(current.getId(), encoded);
            session.invalidate();
            return "redirect:/login";
        }
        model.addAttribute("user", fresh);
        return "profile";
    }
}