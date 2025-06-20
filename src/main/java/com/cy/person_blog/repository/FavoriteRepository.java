package com.cy.person_blog.repository;

import com.cy.person_blog.entity.Favorite;
import com.cy.person_blog.entity.Favorite.FavoriteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    Optional<Favorite> findByUserIdAndArticleIdAndType(Integer userId, Integer articleId, FavoriteType type);

    long countByArticleIdAndType(Integer articleId, FavoriteType type);

    boolean existsByUserIdAndArticleIdAndType(Integer userId, Integer articleId, FavoriteType type);

    void deleteByUserIdAndArticleIdAndType(Integer userId, Integer articleId, FavoriteType type);
    // FavoriteRepository.java 新增查询方法
    @Query("""
      SELECT DATE(f.createdAt) AS date, COUNT(f)
      FROM Favorite f
      JOIN Article a ON f.articleId = a.id
      WHERE a.authorId = :authorId
        AND f.type = :type
        AND DATE(f.createdAt) BETWEEN :start AND :end
      GROUP BY DATE(f.createdAt)
      ORDER BY DATE(f.createdAt)
      """)
    List<Object[]> countDailyStatsByAuthorAndType(
            @Param("authorId") Integer authorId,
            @Param("type")       Favorite.FavoriteType type,
            @Param("start")      Date start,
            @Param("end")        Date end
    );
}
