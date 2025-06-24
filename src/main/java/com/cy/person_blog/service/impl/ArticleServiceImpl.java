// ArticleServiceImpl.java
package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.Article;
import com.cy.person_blog.entity.Article.Status;
import com.cy.person_blog.repository.ArticleRepository;
import com.cy.person_blog.repository.FavoriteRepository;
import com.cy.person_blog.service.ArticleService;
import com.cy.person_blog.service.UserService;
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

    @Autowired private UserService userService;
    @Autowired private FavoriteRepository favoriteRepo;


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

        Set<Integer> addedIds = new HashSet<>();
        List<Article> candidates = new ArrayList<>();

        // —— 1. 标签推荐 ——
        String tags = current.getTags();
        if (tags != null && !tags.trim().isEmpty()) {
            for (String t : tags.split(",")) {
                String pattern = "%" + t.trim() + "%";
                List<Article> byTag = articleRepo
                        .findTop5ByStatusAndTagsContainingAndIdNotOrderByCreatedAtDesc(
                                Article.Status.PUBLISHED, pattern, articleId
                        );
                for (Article a : byTag) {
                    if (addedIds.add(a.getId())) {
                        candidates.add(a);
                    }
                }
            }
        }

        // —— 2. 同分类推荐 ——
        Integer catId = current.getCategoryId();
        if (catId != null) {
            List<Article> byCat = articleRepo
                    .findTop5ByStatusAndCategoryIdAndIdNotOrderByCreatedAtDesc(
                            Article.Status.PUBLISHED, catId, articleId
                    );
            for (Article a : byCat) {
                if (addedIds.add(a.getId())) {
                    candidates.add(a);
                }
            }
        }

        // —— 3. 全部候选按发布时间倒序、截取前 topCount 条 ——
        List<Article> sorted = candidates.stream()
                .sorted(Comparator.comparing(Article::getCreatedAt).reversed())
                .limit(topCount)
                .collect(Collectors.toList());

        // —— 4. （可选）为每条推荐填充首图和摘要 ——
        Pattern IMG = Pattern.compile("<img[^>]*src=[\"']([^\"']+)[\"'][^>]*>", Pattern.CASE_INSENSITIVE);
        Pattern TAG = Pattern.compile("(?s)<[^>]*>");
        for (Article rel : sorted) {
            String html = rel.getContent();
            if (html != null) {
                Matcher m = IMG.matcher(html);
                if (m.find()) {
                    String src = m.group(1).replaceAll("^\\.\\./+", "/");
                    if (!src.startsWith("/")) src = "/" + src;
                    rel.setFirstImageUrl(src);
                }
                String text = TAG.matcher(html).replaceAll("").trim();
                rel.setSummaryText(text.length() > 100
                        ? text.substring(0, 100) + "..."
                        : text);
            }
        }

        return sorted;
    }


    @Override
    public List<Article> getPopularByViews(int limit) {
        Pageable p = PageRequest.of(0, limit);
        List<Article> list = articleRepo.findPopularByViews(p);
        // 只需填 authorName，viewCount 已在 entity
        for (Article a : list) {
            a.setAuthorName(userService.findById(a.getAuthorId()).getNickname());
        }
        return list;
    }

    @Override
    public List<Article> getPopularByLikes(int limit) {
        Pageable p = PageRequest.of(0, limit);
        List<Object[]> rows = articleRepo.findPopularByLikes(p);
        List<Article> list = new ArrayList<>();
        for (Object[] row : rows) {
            Article a = (Article) row[0];
            Long cnt = (Long) row[1];
            // 填充 transient
            a.setLikeCount(cnt);
            a.setAuthorName(userService.findById(a.getAuthorId()).getNickname());
            list.add(a);
        }
        return list;
    }

    @Override
    public List<Article> getPopularByFavorites(int limit) {
        Pageable p = PageRequest.of(0, limit);
        List<Object[]> rows = articleRepo.findPopularByFavorites(p);
        List<Article> list = new ArrayList<>();
        for (Object[] row : rows) {
            Article a = (Article) row[0];
            Long cnt = (Long) row[1];
            a.setFavoriteCount(cnt);
            a.setAuthorName(userService.findById(a.getAuthorId()).getNickname());
            list.add(a);
        }
        return list;
    }


}

