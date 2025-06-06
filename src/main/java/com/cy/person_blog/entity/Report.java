package com.cy.person_blog.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reporter_id", nullable = false)
    private Integer reporterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Integer targetId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 举报对象类型枚举
    public enum TargetType {
        ARTICLE,    // 文章
        COMMENT     // 评论
    }

    // 举报状态枚举
    public enum ReportStatus {
        PENDING,    // 待处理
        RESOLVED    // 已处理
    }

    // 非数据库字段，用于存储举报人信息
    @Transient
    private User reporter;

    // 非数据库字段，用于存储被举报的文章或评论
    @Transient
    private Object target; // 可以是Article或Comment对象
}
