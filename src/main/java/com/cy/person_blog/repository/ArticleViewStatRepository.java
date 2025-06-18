package com.cy.person_blog.repository;

import com.cy.person_blog.entity.ArticleViewStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ArticleViewStatRepository extends JpaRepository<ArticleViewStat, Integer> {

    @Query("""
        SELECT avs.viewDate, SUM(avs.viewCount)
        FROM ArticleViewStat avs
        JOIN Article a ON avs.articleId = a.id
        WHERE a.authorId = :authorId
          AND avs.viewDate BETWEEN :start AND :end
        GROUP BY avs.viewDate
        ORDER BY avs.viewDate
        """)
    List<Object[]> sumDailyViewsByAuthor(
            @Param("authorId") Integer authorId,
            @Param("start") LocalDate start,
            @Param("end")   LocalDate end
    );

    @Modifying
    @Query(value = "INSERT INTO article_view_stat (article_id, view_date, view_count) " +
                   "VALUES (:articleId, :date, 1) " +
                   "ON DUPLICATE KEY UPDATE view_count = view_count + 1",
           nativeQuery = true)
    void incrementDailyCount(@Param("articleId") Integer articleId,
                             @Param("date") LocalDate date);
}
