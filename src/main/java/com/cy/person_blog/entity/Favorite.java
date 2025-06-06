package com.cy.person_blog.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "favorite")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "article_id", nullable = false)
    private Integer articleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FavoriteType type;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 枚举类型定义
    public enum FavoriteType {
        LIKE,       // 点赞
        FAVORITE    // 收藏
    }

    // 非数据库字段，用于存储用户信息
    @Transient
    private User user;

    // 非数据库字段，用于存储文章信息
    @Transient
    private Article article;
}
