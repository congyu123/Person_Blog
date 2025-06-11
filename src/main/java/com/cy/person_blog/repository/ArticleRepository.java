package com.cy.person_blog.repository;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Article.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}