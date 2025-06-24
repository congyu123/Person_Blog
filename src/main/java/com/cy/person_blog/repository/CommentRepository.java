package com.cy.person_blog.repository;

import com.cy.person_blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Date;

public interface CommentRepository  extends JpaRepository<Comment,Integer> {
    List<Comment> findByArticleIdAndParentIdIsNullOrderByCreatedAtAsc(Integer articleId);

    List<Comment> findByParentIdOrderByCreatedAtAsc(Integer parentId);
    
    @Query("SELECT DATE(c.createdAt) as d, COUNT(c) FROM Comment c WHERE DATE(c.createdAt) BETWEEN :start AND :end GROUP BY d ORDER BY DATE(c.createdAt) ASC")
    List<Object[]> countDailyCommentBetween(@Param("start") Date start, @Param("end") Date end);

}
