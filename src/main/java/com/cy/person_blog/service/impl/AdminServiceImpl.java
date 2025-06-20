package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.*;
import com.cy.person_blog.repository.*;
import com.cy.person_blog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired private ArticleRepository articleRepo;
    @Autowired private CommentRepository commentRepo;
    @Autowired private ReportRepository reportRepo;
    @Autowired private UserRepository userRepo;

    @Override public long getArticleCount()   { return articleRepo.count(); }
    @Override public long getCommentCount()   { return commentRepo.count(); }
    @Override public long getVisitCount()     { return articleRepo.sumViewCount(); }

    @Override
    public List<Article> getPendingArticles() {
        return articleRepo.findByStatus(Article.Status.PENDING);
    }

    @Override
    public List<Report> getPendingReports() {
        return reportRepo.findByStatus(Report.ReportStatus.PENDING);
    }

    @Override
    public List<User> getNonAdminUsers() {
        return userRepo.findByIsAdminFalse();
    }

    @Override @Transactional
    public void approveArticle(Integer id) {
        articleRepo.updateStatusById(id, Article.Status.PUBLISHED);
    }

    @Override @Transactional
    public void rejectArticle(Integer id) {
        articleRepo.findById(id).ifPresent(a -> {
            a.setStatus(Article.Status.DRAFT);
            articleRepo.save(a);
        });
    }

    @Override @Transactional
    public void resolveReport(Integer id) {
        reportRepo.findById(id).ifPresent(r -> {
            r.setStatus(Report.ReportStatus.RESOLVED);
            reportRepo.save(r);
        });
    }

    @Override @Transactional
    public void grantAdmin(Integer id) {
        userRepo.findById(id).ifPresent(u -> {
            u.setAdmin(true);
            userRepo.save(u);
        });
    }
    @Override
    @Transactional
    public void handleReport(Integer reportId, boolean isValid) {
        reportRepo.findById(reportId).ifPresent(r -> {

            if (isValid) {
                if (r.getTargetType() == Report.TargetType.ARTICLE) {
                    articleRepo.deleteById(r.getTargetId());
                } else {
                    commentRepo.deleteById(r.getTargetId());
                }
            }

            r.setStatus(Report.ReportStatus.RESOLVED);
            reportRepo.save(r);
        });
    }
    @Override
    public List<Report> getResolvedReports() {
        return reportRepo.findByStatus(Report.ReportStatus.RESOLVED);
    }
}
