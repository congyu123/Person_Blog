package com.cy.person_blog.service;

import com.cy.person_blog.entity.Favorite;
import com.cy.person_blog.entity.Favorite.FavoriteType;

public interface FavoriteService {

    boolean hasFavorite(Integer userId, Integer articleId, FavoriteType type);

    long countFavorites(Integer articleId, FavoriteType type);

    void addFavorite(Integer userId, Integer articleId, FavoriteType type);

    void removeFavorite(Integer userId, Integer articleId, FavoriteType type);

}
