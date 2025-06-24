package com.cy.person_blog.service;


import com.cy.person_blog.entity.Article;
import com.cy.person_blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleService {
    List<Article> getByAuthorAndStatus(Integer authorId, Article.Status status);
    Article findById(Integer id);
    void saveOrUpdate(Article article);
    List<Article> findByAuthor(Integer authorId);
    List<Article> findDrafts(Integer authorId);
    Page<Article> findPublishedArticles(Pageable pageable);

    Page<Article> listPublishedArticles(int page, int size,
                                        String sortField, String sortDir,
                                        Integer categoryId, String tagKeyword);
    Article getPublishedArticleById(Integer id);
    void addViewCount(Integer articleId);
    List<Article> listRelatedArticles(Integer articleId, int topCount);
    List<Article> getPopularByViews(int limit);
    List<Article> getPopularByLikes(int limit);
    List<Article> getPopularByFavorites(int limit);
}