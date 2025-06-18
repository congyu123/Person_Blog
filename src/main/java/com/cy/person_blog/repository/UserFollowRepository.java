package com.cy.person_blog.repository;

import com.cy.person_blog.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Integer> {
    boolean existsByFollowerIdAndFolloweeId(Integer followerId, Integer followeeId);
    long countByFolloweeId(Integer followeeId);
    long countByFollowerId(Integer followerId);
    void deleteByFollowerIdAndFolloweeId(Integer followerId, Integer followeeId);
    List<UserFollow> findByFollowerId(Integer followerId);
    List<UserFollow> findByFolloweeId(Integer followeeId);

}
