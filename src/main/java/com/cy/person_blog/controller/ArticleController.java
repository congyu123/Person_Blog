// ArticleController.java
package com.cy.person_blog.controller;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Category;
import com.cy.person_blog.entity.Comment;
import com.cy.person_blog.service.ArticleService;
import com.cy.person_blog.service.CategoryService;
import com.cy.person_blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired private ArticleService articleService;
    @Autowired private CategoryService categoryService;
    @Autowired
    private CommentService commentService;

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
        List<Category> categories = categoryService.listAllCategories();
        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        return "publish_article";
    }

    // 保存（包括草稿箱和发布）
    @PostMapping("/save")
    public String save(@ModelAttribute Article article,
                       @RequestParam String action,
                       HttpSession session) {

        Integer userId = ((com.cy.person_blog.entity.User)
                session.getAttribute("currentUser")).getId();
        article.setAuthorId(userId);
        article.setStatus(Article.Status.valueOf("draft".equals(action) ? "DRAFT" : "PENDING"));
        articleService.saveOrUpdate(article);
        return "redirect:/profile";
    }
    @GetMapping("/{id}")
    public String viewArticleDetail(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Article article = articleService.getPublishedArticleById(id);

        articleService.addViewCount(id);

        List<Comment> comments = commentService.listCommentsByArticleId(id);

        List<Category> categories = categoryService.listAllCategories();

        List<Article> related = articleService.listRelatedArticles(id, 5);

        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        model.addAttribute("categories", categories);
        model.addAttribute("relatedArticles", related);
        model.addAttribute("newComment", new Comment());

        return "article_detail";
    }
    @PostMapping("/comment")
    public String postComment(@ModelAttribute("newComment") Comment comment) {
        commentService.addComment(comment);
        return "redirect:/articles/" + comment.getArticleId();
    }
}
