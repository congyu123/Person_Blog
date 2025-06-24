package com.cy.person_blog.controller;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.User;
import com.cy.person_blog.service.ArticleService;
import com.cy.person_blog.service.FollowService;
import com.cy.person_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/users")
public class FollowController {
    @Autowired
    private FollowService followService;
    @Autowired private UserService userService;
    @Autowired private ArticleService articleService;
    @PostMapping("/{id}/follow")
    @ResponseBody
    public String toggleFollow(@PathVariable Integer id, HttpSession session) {
        User me = (User) session.getAttribute("currentUser");
        if (me == null) return "error:not_logged_in";
        if (followService.isFollowing(me.getId(), id)) {
            followService.unfollow(me.getId(), id);
        } else {
            followService.follow(me.getId(), id);
        }
        return String.valueOf(followService.getFollowerCount(id));
    }

    @GetMapping("/followers")
    public String listFollowers(HttpSession session, Model model) {
        User me = (User) session.getAttribute("currentUser");
        model.addAttribute("followers", followService.getFollowers(me.getId()));
        return "article_detail";
    }


    @GetMapping("/following")
    public String listFollowing(HttpSession session, Model model) {
        User me = (User) session.getAttribute("currentUser");
        if (me == null) return "redirect:/login";
        // followService.getFollowing 返回 List<User>
        model.addAttribute("following", followService.getFollowing(me.getId()));
        return "following_list";
    }

    // —— 查看某人主页 ——
    @GetMapping("/{id}")
    public String viewUserProfile(@PathVariable Integer id,
                                  HttpSession session,
                                  Model model) {
        User target = userService.findById(id);
        // TA 的基本信息
        model.addAttribute("userProfile", target);
        // TA 的文章
        List<Article> list = articleService.getByAuthorAndStatus(id, Article.Status.PUBLISHED);
        model.addAttribute("articles", list);
        // 统计粉丝／关注
        model.addAttribute("followerCount", followService.getFollowerCount(id));
        model.addAttribute("followingCount", followService.countFollowing(id));
        // 当前用户是否已关注 TA
        User me = (User) session.getAttribute("currentUser");
        boolean isFollowing = me != null && followService.isFollowing(me.getId(), id);
        model.addAttribute("isFollowing", isFollowing);
        return "user_profile";
    }}