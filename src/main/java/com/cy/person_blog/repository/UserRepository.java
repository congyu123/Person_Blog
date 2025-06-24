package com.cy.person_blog.repository;

import com.cy.person_blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByIsAdminFalse();

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.followerCount = u.followerCount + 1 WHERE u.id = :userId")
    void incrementFollowerCount(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query(
            value = "UPDATE `user` SET follower_count = follower_count - 1 " +
                    "WHERE id = :userId AND follower_count > 0",
            nativeQuery = true
    )
    void decrementFollowerCount(@Param("userId") Integer userId);}