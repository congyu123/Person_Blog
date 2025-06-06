package com.cy.person_blog.service;

import com.cy.person_blog.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentsByArticleId(Integer articleId);

    /**
     * 新增评论或回复
     */
    void addComment(Comment comment);
}
