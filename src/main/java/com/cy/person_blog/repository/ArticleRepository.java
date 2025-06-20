package com.cy.person_blog.repository;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Article.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByAuthorId(Integer authorId);
    List<Article> findByAuthorIdAndStatus(Integer authorId, Status status);
    Page<Article> findByStatus(Article.Status status, Pageable pageable);
    Page<Article> findByStatusAndCategoryIdAndTagsContaining(
            Article.Status status,
            Integer categoryId,
            String tag,
            Pageable pageable
    );

    Article findByIdAndStatus(Integer id, Article.Status status);

    List<Article> findTop5ByStatusAndTagsContainingAndIdNotOrderByCreatedAtDesc(
            Article.Status status,
            String tagKeyword,
            Integer excludeId
    );
    @Query("select a from Article a where a.status = :status "
            + "and (:categoryId is null or a.categoryId = :categoryId) "
            + "and (:tag is null or a.tags like %:tag%)")
    Page<Article> findByStatusAndOptionalCategoryIdAndTag(@Param("status") Status status,
                                                          @Param("categoryId") Integer categoryId,
                                                          @Param("tag") String tag,
                                                          Pageable pageable);
    @Query("select a from Article a where a.status = :status "
            + "and (:categoryId is null or a.categoryId = :categoryId) "
            + "and (:keyword is null or (a.tags like %:keyword% or a.title like %:keyword%))")
    Page<Article> findByStatusAndOptionalCategoryIdAndKeyword(@Param("status") Status status,
                                                              @Param("categoryId") Integer categoryId,
                                                              @Param("keyword") String keyword,
                                                              Pageable pageable);
    @Query("SELECT DATE(a.createdAt) AS date, SUM(a.viewCount) " +
            "FROM Article a " +
            "WHERE a.authorId = :authorId " +
            "AND a.status = 'PUBLISHED' " +
            "AND DATE(a.createdAt) BETWEEN :start AND :end " +
            "GROUP BY DATE(a.createdAt) " +
            "ORDER BY DATE(a.createdAt)")
    List<Object[]> sumDailyViewsByAuthor(
            @Param("authorId") Integer authorId,
            @Param("start") Date start,
            @Param("end") Date end);

    @Modifying
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :articleId")
    void incrementViewCount(@Param("articleId") Integer articleId);
    List<Article> findByStatus(Status status);
    @Query("SELECT COALESCE(SUM(a.viewCount),0) FROM Article a")
    Long sumViewCount();
    @Modifying
    @Query("UPDATE Article a SET a.status = :status WHERE a.id = :id")
    int updateStatusById(@Param("id") Integer id,
                         @Param("status") Article.Status status);
}