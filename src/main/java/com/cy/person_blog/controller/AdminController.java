package com.cy.person_blog.controller;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Report;
import com.cy.person_blog.entity.User;
import com.cy.person_blog.repository.ReportRepository;
import com.cy.person_blog.service.AdminService;
import com.cy.person_blog.service.ArticleService;
import com.cy.person_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private AdminService adminService;
    @Autowired private ReportRepository reportRepo;
    @Autowired private ArticleService articleService;
    @Autowired private UserService userService;
    @GetMapping({"", "/"})
    public String admin(Model model) {
        model.addAttribute("articleCount",    adminService.getArticleCount());
        model.addAttribute("commentCount",    adminService.getCommentCount());
        model.addAttribute("visitCount",      adminService.getVisitCount());
        model.addAttribute("pendingArticles", adminService.getPendingArticles());
        model.addAttribute("pendingReports",  adminService.getPendingReports());
        model.addAttribute("users",           adminService.getNonAdminUsers());
        model.addAttribute("resolvedReports", adminService.getResolvedReports());
        return "admin";
    }
    @GetMapping("/reports/{id}")
    public String reviewReport(@PathVariable("id") Integer reportId, Model model) {
        Report rpt = reportRepo.findById(reportId).orElseThrow();
        model.addAttribute("report", rpt);
        if (rpt.getTargetType() == Report.TargetType.ARTICLE) {
            Article art = articleService.findById(rpt.getTargetId());
            model.addAttribute("targetArticle", art);
        }
        User reporter = userService.findById(rpt.getReporterId());
        model.addAttribute("reporter", reporter);
        return "/report_review";
    }
    @PostMapping("/reports/{id}/handle")
    public String handleReport(
            @PathVariable("id") Integer id,
            @RequestParam("valid") boolean valid
    ) {
        adminService.handleReport(id, valid);
        return "redirect:/admin";
    }
    @PostMapping("/articles/{id}/approve")
    public String approveArticle(@PathVariable Integer id) {
        adminService.approveArticle(id);
        return "redirect:/admin";
    }

    @PostMapping("/articles/{id}/reject")
    public String rejectArticle(@PathVariable Integer id) {
        adminService.rejectArticle(id);
        return "redirect:/admin";
    }

    @PostMapping("/reports/{id}/resolve")
    public String resolveReport(@PathVariable Integer id) {
        adminService.resolveReport(id);
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/grant")
    public String grantAdmin(@PathVariable Integer id) {
        adminService.grantAdmin(id);
        return "redirect:/admin";
    }
}
