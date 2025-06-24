package com.cy.person_blog.service;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Favorite;
import com.cy.person_blog.entity.Favorite.FavoriteType;

import java.util.List;
import java.util.Map;

public interface FavoriteService {

    boolean hasFavorite(Integer userId, Integer articleId, FavoriteType type);

    long countFavorites(Integer articleId, FavoriteType type);

    void addFavorite(Integer userId, Integer articleId, FavoriteType type);

    void removeFavorite(Integer userId, Integer articleId, FavoriteType type);
    Map<String, Long> countLikesAndFavoritesByAuthor(Integer authorId);

    Map<String, Object> getInteractionStats(Integer userId, String period);
    List<Article> listUserFavoriteArticles(Integer userId, Favorite.FavoriteType type);
}
