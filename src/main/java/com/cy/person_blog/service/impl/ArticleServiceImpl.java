// ArticleServiceImpl.java
package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Article.Status;
import com.cy.person_blog.repository.ArticleRepository;
import com.cy.person_blog.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @Override
    public Page<Article> findPublishedArticles(Pageable pageable) {
        return articleRepo.findByStatus(Article.Status.PUBLISHED, pageable);
    }
    private static final Pattern IMG_PATTERN =
            Pattern.compile("<img[^>]*src=[\"']([^\"']+)[\"'][^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern TAG_PATTERN =
            Pattern.compile("(?s)<[^>]*>");
    @Override
    public Page<Article> listPublishedArticles(int page, int size,
                                               String sortField, String sortDir,
                                               Integer categoryId, String keyword) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Integer catParam = (categoryId == null || categoryId == 0) ? null : categoryId;
        String key = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();

        Page<Article> pageData = articleRepo
                .findByStatusAndOptionalCategoryIdAndKeyword(
                        Article.Status.PUBLISHED, catParam, key, pageable);

        for (Article art : pageData) {
            String html = art.getContent();
            if (html != null) {
                // —— 提取首张图片 ——
                Matcher imgM = IMG_PATTERN.matcher(html);
                if (imgM.find()) {
                    String raw = imgM.group(1);
                    // "../uploads/…" → "/uploads/…"
                    String normalized = raw.replaceAll("^\\.\\./+", "/");
                    if (!normalized.startsWith("/")) {
                        normalized = "/" + normalized;
                    }
                    art.setFirstImageUrl(normalized);
                }

                // —— 提取纯文字摘要 ——

                String text = TAG_PATTERN.matcher(html).replaceAll("").trim();
                if (!text.isEmpty()) {
                    if (text.length() > 150) {
                        text = text.substring(0, 150) + "...";
                    }
                    art.setSummaryText(text);
                }
            }
        }

        return pageData;
    }


    @Override
    public Article getPublishedArticleById(Integer id) {
        return articleRepo.findByIdAndStatus(id, Article.Status.PUBLISHED);
    }

    @Override
    public void addViewCount(Integer articleId) {
        Article a = articleRepo.findById(articleId).orElse(null);
        if (a != null) {
            a.setViewCount(a.getViewCount() + 1);
            articleRepo.save(a);
        }
    }

    @Override
    public List<Article> listRelatedArticles(Integer articleId, int topCount) {
        Article current = articleRepo.findById(articleId).orElse(null);
        if (current == null) return Collections.emptyList();

        String tags = current.getTags(); // 形如： "SpringBoot,Java"
        if (tags == null || tags.trim().isEmpty()) {
            return Collections.emptyList();
        }

        // 拆分多个标签
        String[] tagArray = tags.split(",");
        Set<Integer> addedIds = new HashSet<>();
        List<Article> allCandidates = new ArrayList<>();

        for (String t : tagArray) {
            String keyword = "%" + t.trim() + "%";
            List<Article> subList = articleRepo
                    .findTop5ByStatusAndTagsContainingAndIdNotOrderByCreatedAtDesc(
                            Article.Status.PUBLISHED, keyword, articleId
                    );
            for (Article aa : subList) {
                if (!addedIds.contains(aa.getId())) {
                    addedIds.add(aa.getId());
                    allCandidates.add(aa);
                }
            }
        }

        List<Article> sorted = allCandidates.stream()
                .sorted(Comparator.comparing(Article::getCreatedAt).reversed())
                .collect(Collectors.toList());

        return sorted.size() <= topCount ? sorted : sorted.subList(0, topCount);
    }

}

