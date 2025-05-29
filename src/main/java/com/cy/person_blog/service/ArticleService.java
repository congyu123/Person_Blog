package com.cy.person_blog.service;


import com.cy.person_blog.entity.Article;
import com.cy.person_blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ArticleService {

    List<Article> getByAuthorAndStatus(Integer authorId, Article.Status status);

    Article findById(Integer id);

    void saveOrUpdate(Article article);

    List<Article> findByAuthor(Integer authorId);

    List<Article> findDrafts(Integer authorId);
}
