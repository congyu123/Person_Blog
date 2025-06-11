package com.cy.person_blog.repository;

import com.cy.person_blog.entity.Favorite;
import com.cy.person_blog.entity.Favorite.FavoriteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Optional<Favorite> findByUserIdAndArticleIdAndType(Integer userId, Integer articleId, FavoriteType type);

    long countByArticleIdAndType(Integer articleId, FavoriteType type);

    boolean existsByUserIdAndArticleIdAndType(Integer userId, Integer articleId, FavoriteType type);

    void deleteByUserIdAndArticleIdAndType(Integer userId, Integer articleId, FavoriteType type);

}
