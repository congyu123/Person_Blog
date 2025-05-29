// ArticleController.java
package com.cy.person_blog.controller;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Category;
import com.cy.person_blog.service.ArticleService;
import com.cy.person_blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired private ArticleService articleService;
    @Autowired private CategoryService categoryService;

    @GetMapping
    public String redirectToNew() {
        return "redirect:/articles/new";
    }

    // 新增/编辑表单
    @GetMapping({"/new", "/edit/{id}"})
    public String form(@PathVariable(required = false) Integer id,
                       Model model, HttpSession session) {
        Article article = (id == null)
                ? new Article()
                : articleService.findById(id);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        return "publish_article";
    }

    // 保存（包括草稿箱和发布）
    @PostMapping("/save")
    public String save(@ModelAttribute Article article,
                       @RequestParam String action,
                       HttpSession session) {
        // 当前用户
        Integer userId = ((com.cy.person_blog.entity.User)
                session.getAttribute("currentUser")).getId();
        article.setAuthorId(userId);
        // action: "draft" 或 "publish"
        article.setStatus(Article.Status.valueOf("draft".equals(action) ? "DRAFT" : "PENDING"));
        articleService.saveOrUpdate(article);
        return "redirect:/profile";
    }
}
