package com.cy.person_blog.repository;

import com.cy.person_blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment,Integer> {
    List<Comment> findByArticleIdAndParentIdIsNullOrderByCreatedAtAsc(Integer articleId);

    List<Comment> findByParentIdOrderByCreatedAtAsc(Integer parentId);
}
