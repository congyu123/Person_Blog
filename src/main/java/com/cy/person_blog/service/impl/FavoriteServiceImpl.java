package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.Favorite;
import com.cy.person_blog.entity.Favorite.FavoriteType;
import com.cy.person_blog.repository.FavoriteRepository;
import com.cy.person_blog.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

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


}
