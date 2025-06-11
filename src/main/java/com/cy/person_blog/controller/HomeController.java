package com.cy.person_blog.controller;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Category;
import com.cy.person_blog.service.ArticleService;
import com.cy.person_blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/")
    public String viewHomePage(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortField", defaultValue = "createdAt") String sortField,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        List<Category> categories = categoryService.listAllCategories();
        model.addAttribute("categories", categories);

        Page<Article> articlePage = articleService.listPublishedArticles(page, size, sortField, sortDir, categoryId, keyword);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("totalItems", articlePage.getTotalElements());
        model.addAttribute("articles", articlePage.getContent());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("selectedCategory", categoryId == null ? 0 : categoryId);
        model.addAttribute("keyword", keyword == null ? "" : keyword);

        return "index";
    }


}
