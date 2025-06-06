package com.cy.person_blog.controller;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.User;
import com.cy.person_blog.service.ArticleService;
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
    @Autowired private ArticleService articleService;

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) {
            return "redirect:/login";
        }

        // 从数据库重新加载完整 User 对象
        User user = userService.findById(current.getId());
        model.addAttribute("user", user);

        // 头像 URL：如果数据库中 avatarUrl 不为空，则使用 /uploads/avatars/{filename}，否则用默认图
        String avatarUrl = (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty())
                ? "/uploads/avatars/" + user.getAvatar_url()
                : "/static/img/default-avatar.png";
        model.addAttribute("userAvatarUrl", avatarUrl);

        Integer userId = user.getId();
        // 查询各状态文章
        model.addAttribute("publishedArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.PUBLISHED));
        model.addAttribute("pendingArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.PENDING));
        model.addAttribute("draftArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.DRAFT));

        return "profile";
    }

    /**
     * 更新个人信息 (昵称、简介、头像)
     * 举例返回 JSON: {"success": true, "avatarUrl": "/uploads/avatars/xxx.png"} 或 {"success": false, "error": "..."}
     */
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
            // 调用 service 保存头像并更新用户表中昵称与简介
            String filename = userService.updateProfile(current.getId(), nickname, bio, avatar);
            // 更新 Session 里的 currentUser
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

    /**
     * 修改密码
     * 如果成功则 session 失效并重定向到 /login
     * 如果失败则在 profile 页面显示错误并回显
     */
    @PostMapping("/profile/password")
    public String updatePassword(HttpSession session,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {

        User current = (User) session.getAttribute("currentUser");
        if (current == null) {
            return "redirect:/login";
        }

        // 重新从数据库加载
        User fresh = userService.findById(current.getId());

        if (!passwordEncoder.matches(oldPassword, fresh.getPassword())) {
            model.addAttribute("errorPwd", "旧密码不正确");
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorPwd", "两次密码不一致");
        } else {
            // 修改密码并立即让用户登出，重定向到登录页
            String encoded = passwordEncoder.encode(newPassword);
            userService.changePassword(current.getId(), encoded);
            session.invalidate();
            return "redirect:/login";
        }

        // 如果走到这里，说明校验失败，需要重新渲染 profile 页面并且带上之前所有 model 属性
        model.addAttribute("user", fresh);

        String avatarUrl = (fresh.getAvatar_url() != null && !fresh.getAvatar_url().isEmpty())
                ? "/uploads/avatars/" + fresh.getAvatar_url()
                : "/static/img/default-avatar.png";
        model.addAttribute("userAvatarUrl", avatarUrl);

        Integer userId = fresh.getId();
        model.addAttribute("publishedArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.PUBLISHED));
        model.addAttribute("pendingArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.PENDING));
        model.addAttribute("draftArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.DRAFT));

        return "profile";
    }
}
