// ArticleServiceImpl.java
package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Article.Status;
import com.cy.person_blog.repository.ArticleRepository;
import com.cy.person_blog.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepo;



    @Override
    public List<Article> getByAuthorAndStatus(Integer authorId, Status status) {
        return articleRepo.findByAuthorIdAndStatus(authorId, status);
    }

    @Override
    public Article findById(Integer id) {
        return articleRepo.findById(id).orElse(null);
    }

    @Override
    public void saveOrUpdate(Article article) {
        articleRepo.save(article);
    }

    @Override
    public List<Article> findByAuthor(Integer authorId) {
        return articleRepo.findByAuthorId(authorId);
    }

    @Override
    public List<Article> findDrafts(Integer authorId) {
        return articleRepo.findByAuthorIdAndStatus(authorId, Status.DRAFT);
    }
}
