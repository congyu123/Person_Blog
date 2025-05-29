package com.cy.person_blog.repository;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Article.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByAuthorId(Integer authorId);
    List<Article> findByAuthorIdAndStatus(Integer authorId, Status status);

}