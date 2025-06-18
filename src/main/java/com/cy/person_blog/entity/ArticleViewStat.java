package com.cy.person_blog.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "article_view_stat")
public class ArticleViewStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="article_id", nullable=false)
    private Integer articleId;

    @Column(name="view_date", nullable=false)
    private LocalDate viewDate;

    @Column(name="view_count", nullable=false)
    private Integer viewCount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public LocalDate getViewDate() {
        return viewDate;
    }

    public void setViewDate(LocalDate viewDate) {
        this.viewDate = viewDate;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

}
