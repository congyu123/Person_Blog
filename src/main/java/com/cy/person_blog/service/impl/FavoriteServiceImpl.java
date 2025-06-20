package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Favorite;
import com.cy.person_blog.entity.Favorite.FavoriteType;
import com.cy.person_blog.repository.ArticleRepository;
import com.cy.person_blog.repository.ArticleViewStatRepository;
import com.cy.person_blog.repository.FavoriteRepository;
import com.cy.person_blog.service.FavoriteService;
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
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private ArticleViewStatRepository viewStatRepo;
    @Override
    public boolean hasFavorite(Integer userId, Integer articleId, FavoriteType type) {
        return favoriteRepository.existsByUserIdAndArticleIdAndType(userId, articleId, type);
    }

    @Override
    public long countFavorites(Integer articleId, FavoriteType type) {
        return favoriteRepository.countByArticleIdAndType(articleId, type);
    }

    @Override
    public void addFavorite(Integer userId, Integer articleId, FavoriteType type) {
        if (!hasFavorite(userId, articleId, type)) {
            Favorite favorite = new Favorite();
            favorite.setUserId(userId);
            favorite.setArticleId(articleId);
            favorite.setType(type);
            favoriteRepository.save(favorite);
        }
    }

    @Override
    @Transactional
    public void removeFavorite(Integer userId, Integer articleId, FavoriteType type) {
        favoriteRepository.deleteByUserIdAndArticleIdAndType(userId, articleId, type);
    }
    @Override
    public Map<String, Long> countLikesAndFavoritesByAuthor(Integer authorId) {
        List<Article> articles = articleRepo.findByAuthorIdAndStatus(authorId, Article.Status.PUBLISHED);

        long likeCount = 0;
        long favoriteCount = 0;
        for (Article a : articles) {
            likeCount += favoriteRepository.countByArticleIdAndType(a.getId(), FavoriteType.LIKE);
            favoriteCount += favoriteRepository.countByArticleIdAndType(a.getId(), FavoriteType.FAVORITE);
        }

        Map<String, Long> result = new HashMap<>();
        result.put("likeCount", likeCount);
        result.put("favoriteCount", favoriteCount);
        return result;
    }
    @Override
    public Map<String, Object> getInteractionStats(Integer userId, String period) {
        Map<String, Object> stats = new HashMap<>();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        switch (period) {
            case "month":
                startDate = endDate.minusDays(29);
                break;
            case "quarter":
                startDate = endDate.minusDays(89);
                break;
            default: // week
                startDate = endDate.minusDays(6);
        }

        List<String> labels = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            labels.add(date.format(DateTimeFormatter.ofPattern("MM-dd")));
        }
        stats.put("labels", labels);

        List<Long> likes = getDailyStats(userId, "LIKE", startDate, endDate);
        stats.put("likes", likes);

        List<Long> favorites = getDailyStats(userId, "FAVORITE", startDate, endDate);
        stats.put("favorites", favorites);

        List<Long> views = getDailyViewStats(userId, startDate, endDate);
        stats.put("views", views);

        return stats;
    }

    private List<Long> getDailyStats(Integer authorId, String type, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = favoriteRepository.countDailyStatsByAuthorAndType(
                authorId,
                FavoriteType.valueOf(type),
                Date.valueOf(startDate),
                Date.valueOf(endDate)
        );

        return fillStats(results, startDate, endDate);
    }

    private List<Long> getDailyViewStats(Integer userId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> rows = viewStatRepo.sumDailyViewsByAuthor(userId, startDate, endDate);
        return fillStats(rows, startDate, endDate);
    }

    private List<Long> fillStats(List<Object[]> results, LocalDate start, LocalDate end) {
        Map<LocalDate, Long> map = new HashMap<>();
        for (Object[] row : results) {
            Object rawDate = row[0];
            LocalDate date;
            if (rawDate instanceof LocalDate) {
                date = (LocalDate) rawDate;
            } else if (rawDate instanceof java.sql.Date) {
                date = ((java.sql.Date) rawDate).toLocalDate();
            } else {
                throw new IllegalArgumentException("Unexpected date type: " + rawDate.getClass());
            }
            Number cnt = (Number) row[1];
            map.put(date, cnt == null ? 0L : cnt.longValue());
        }

        List<Long> stats = new ArrayList<>();
        for (LocalDate cursor = start; !cursor.isAfter(end); cursor = cursor.plusDays(1)) {
            stats.add(map.getOrDefault(cursor, 0L));
        }
        return stats;
    }

}
