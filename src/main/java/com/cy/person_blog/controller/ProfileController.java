package com.cy.person_blog.controller;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Favorite;
import com.cy.person_blog.entity.User;
import com.cy.person_blog.service.ArticleService;
import com.cy.person_blog.service.FavoriteService;
import com.cy.person_blog.service.FollowService;
import com.cy.person_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ProfileController {

    @Autowired private UserService userService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private ArticleService articleService;
    @Autowired private FavoriteService favoriteService;
    @Autowired private FollowService followService;

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User current = (User) session.getAttribute("currentUser");
        if (current == null) {
            return "redirect:/login";
        }

        User user = userService.findById(current.getId());
        model.addAttribute("user", user);

        String avatarUrl = (user.getAvatar_url() != null && !user.getAvatar_url().isEmpty())
                ? "/uploads/avatars/" + user.getAvatar_url()
                : "static/img/default-avatar.png";
        model.addAttribute("userAvatarUrl", avatarUrl);

        Integer userId = user.getId();
        model.addAttribute("publishedArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.PUBLISHED));
        model.addAttribute("pendingArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.PENDING));
        model.addAttribute("draftArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.DRAFT));

        List<Article> published = articleService.getByAuthorAndStatus(user.getId(), Article.Status.PUBLISHED);
        long totalViews = published.stream()
                .mapToLong(Article::getViewCount)
                .sum();
        model.addAttribute("viewCount", totalViews);

        Map<String, Long> stats = favoriteService.countLikesAndFavoritesByAuthor(userId);
        model.addAttribute("likeCount", stats.get("likeCount"));
        model.addAttribute("favoriteCount", stats.get("favoriteCount"));
        long followerCount  = followService.getFollowerCount(userId);
        long followingCount = followService.countFollowing(userId);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followingList", followService.getFollowing(userId));

        List<Article> likedArticles =
                favoriteService.listUserFavoriteArticles(userId, Favorite.FavoriteType.LIKE);
        List<Article> favoritedArticles =
                favoriteService.listUserFavoriteArticles(userId, Favorite.FavoriteType.FAVORITE);

        model.addAttribute("likedArticles", likedArticles);
        model.addAttribute("favoritedArticles", favoritedArticles);
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
            String filename = userService.updateProfile(current.getId(), nickname, bio, avatar);
            User updated = userService.findById(current.getId());
            session.setAttribute("currentUser", updated);

            resp.put("success", true);
            resp.put("avatarUrl", "/uploads/avatars" + filename);
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
        if (current == null) {
            return "redirect:/login";
        }

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

        String avatarUrl = (fresh.getAvatar_url() != null && !fresh.getAvatar_url().isEmpty())
                ? "/uploads/avatars/" + fresh.getAvatar_url()
                : "static/img/default-avatar.png";
        model.addAttribute("userAvatarUrl", avatarUrl);

        Integer userId = fresh.getId();
        model.addAttribute("publishedArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.PUBLISHED));
        model.addAttribute("pendingArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.PENDING));
        model.addAttribute("draftArticles",
                articleService.getByAuthorAndStatus(userId, Article.Status.DRAFT));
        long followingCount = followService.getFollowerCount(userId);
        long followerCount  = followService.getFollowerCount(userId);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount",  followerCount);
        return "profile";
    }
    @GetMapping("/profile/stats")
    @ResponseBody
    public Map<String, Object> getInteractionStats(
            HttpSession session,
            @RequestParam(defaultValue = "7") int days) {

        Map<String, Object> response = new HashMap<>();
        User current = (User) session.getAttribute("currentUser");
        if (current == null) {
            response.put("success", false);
            response.put("error", "未登录");
            return response;
        }

        try {
            Map<String, Object> stats = favoriteService.getInteractionStats(current.getId(), String.valueOf(days));
            response.put("success", true);
            response.put("data", stats);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
    @GetMapping("/{id}")
    public String viewUserProfile(@PathVariable Integer id,
                                  Model model,
                                  HttpSession session) {
        User user = userService.findById(id);
        model.addAttribute("user", user);

        List<Article> published = articleService.getByAuthorAndStatus(id, Article.Status.PUBLISHED);
        List<Article> pending   = articleService.getByAuthorAndStatus(id, Article.Status.PENDING);
        model.addAttribute("publishedArticles", published);
        model.addAttribute("pendingArticles", pending);

        long followerCount  = followService.getFollowerCount(id);
        long followingCount = followService.countFollowing(id);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("followingCount", followingCount);

        User me = (User) session.getAttribute("currentUser");
        boolean isFollowing = me != null && followService.isFollowing(me.getId(), id);
        model.addAttribute("isFollowing", isFollowing);

        return "user_profile";
    }
}
