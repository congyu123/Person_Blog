package com.cy.person_blog.service;

import com.cy.person_blog.entity.Article;

import java.util.List;

public interface ArticleService {
    Article findById(Integer id);

    void saveOrUpdate(Article article);

    List<Article> findByAuthor(Integer authorId);

    List<Article> findDrafts(Integer authorId);
}
