package com.cy.person_blog.controller;

import com.cy.person_blog.entity.User;
import com.cy.person_blog.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class FollowController {
    @Autowired
    private FollowService followService;

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
        model.addAttribute("following", followService.getFollowing(me.getId()));
        return "article_detail";
    }
}