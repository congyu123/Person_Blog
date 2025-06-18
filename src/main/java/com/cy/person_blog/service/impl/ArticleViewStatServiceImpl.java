package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.ArticleViewStat;
import com.cy.person_blog.repository.ArticleRepository;
import com.cy.person_blog.repository.ArticleViewStatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ArticleViewStatServiceImpl implements com.cy.person_blog.service.ArticleViewStatService {

    @Autowired
    private ArticleViewStatRepository viewStatRepo;

    @Autowired
    private ArticleRepository articleRepo;

    @Override
    @Transactional
    public void recordView(Integer articleId) {
        LocalDate today = LocalDate.now();

        viewStatRepo.incrementDailyCount(articleId, today);

        articleRepo.incrementViewCount(articleId);
    }
}
