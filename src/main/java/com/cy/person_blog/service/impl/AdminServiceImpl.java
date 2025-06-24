package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.*;
import com.cy.person_blog.repository.*;
import com.cy.person_blog.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Override
    public List<String> getLast7DaysLabels() {
        List<String> days = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
        for (int i = 6; i >= 0; i--) {
            days.add(today.minusDays(i).format(fmt));
        }
        return days;
    }

    // 近7天每日新增文章数
    @Override
    public List<Integer> getLast7DaysArticleCount() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        // 数据库查询结果：[date, count]
        List<Object[]> raw = articleRepo.countDailyArticleBetween(Date.valueOf(start), Date.valueOf(today));
        return fillDateGap(raw, start, today);
    }

    @Override
    public List<Integer> getLast7DaysCommentCount() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        List<Object[]> raw = commentRepo.countDailyCommentBetween(Date.valueOf(start), Date.valueOf(today));
        return fillDateGap(raw, start, today);
    }

    @Override
    public List<Integer> getLast7DaysVisitCount() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        List<Object[]> raw = articleRepo.countDailyViewBetween(Date.valueOf(start), Date.valueOf(today));
        return fillDateGap(raw, start, today);
    }

    private List<Integer> fillDateGap(List<Object[]> dbResult, LocalDate start, LocalDate end) {
        Map<String, Integer> map = new HashMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
        for (Object[] row : dbResult) {
            String day = ((java.sql.Date) row[0]).toLocalDate().format(fmt);
            Integer cnt = ((Number) row[1]).intValue();
            map.put(day, cnt);
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            String key = end.minusDays(i).format(fmt);
            result.add(map.getOrDefault(key, 0));
        }
        return result;
    }
}
