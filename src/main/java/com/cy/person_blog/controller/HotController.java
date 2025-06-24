package com.cy.person_blog.controller;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/hot")
public class HotController {

    private final ArticleService articleService;
    private static final int TOP_LIMIT = 10;

    @Autowired
    public HotController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/views")
    public String hotByViews(Model model) {
        List<Article> articles = articleService.getPopularByViews(TOP_LIMIT);
        model.addAttribute("articles", articles);
        model.addAttribute("title", "浏览榜");
        model.addAttribute("icon", "eye");
        return "hot_list";
    }

    @GetMapping("/likes")
    public String hotByLikes(Model model) {
        List<Article> articles = articleService.getPopularByLikes(TOP_LIMIT);
        model.addAttribute("articles", articles);
        model.addAttribute("title", "点赞榜");
        model.addAttribute("icon", "thumbs-up");
        return "hot_list";
    }

    @GetMapping("/favorites")
    public String hotByFavorites(Model model) {
        List<Article> articles = articleService.getPopularByFavorites(TOP_LIMIT);
        model.addAttribute("articles", articles);
        model.addAttribute("title", "收藏榜");
        model.addAttribute("icon", "star");
        return "hot_list";
    }
}