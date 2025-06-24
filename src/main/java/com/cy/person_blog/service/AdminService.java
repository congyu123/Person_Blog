package com.cy.person_blog.service;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Report;
import com.cy.person_blog.entity.User;
import java.util.List;

public interface AdminService {
    long getArticleCount();
    long getCommentCount();
    long getVisitCount();

    List<Article> getPendingArticles();
    List<Report> getPendingReports();
    List<User> getNonAdminUsers();

    void approveArticle(Integer articleId);
    void rejectArticle(Integer articleId);
    void resolveReport(Integer reportId);
    void grantAdmin(Integer userId);

    void handleReport(Integer reportId, boolean isValid);
    List<Report> getResolvedReports();
    List<Integer> getLast7DaysArticleCount();
    List<Integer> getLast7DaysCommentCount();
    List<Integer> getLast7DaysVisitCount();
    List<String> getLast7DaysLabels();
}
