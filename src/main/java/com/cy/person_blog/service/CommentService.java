package com.cy.person_blog.service;

import com.cy.person_blog.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentsByArticleId(Integer articleId);


    void addComment(Comment comment);
}
