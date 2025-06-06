package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.Comment;
import com.cy.person_blog.entity.User;
import com.cy.person_blog.repository.CommentRepository;
import com.cy.person_blog.repository.UserRepository;
import com.cy.person_blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Comment> listCommentsByArticleId(Integer articleId) {
        List<Comment> topComments = commentRepository
                .findByArticleIdAndParentIdIsNullOrderByCreatedAtAsc(articleId);

        for (Comment c : topComments) {

            User u = c.getUser();
            if (u != null) {
                u.getNickname(); u.getAvatar_url();

            }

            List<Comment> childReplies = commentRepository.findByParentIdOrderByCreatedAtAsc(c.getId());
            c.getReplies().clear();
            c.getReplies().addAll(childReplies);
            for (Comment reply : childReplies) {
                User ru = reply.getUser();
                if (ru != null) {
                    ru.getNickname(); ru.getAvatar_url();
                }
            }
        }

        return topComments;
    }

    @Override
    @Transactional
    public void addComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(comment);
    }
}
