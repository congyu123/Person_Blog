package com.cy.person_blog.service;

import com.cy.person_blog.entity.User;

import java.util.List;

public interface FollowService {
    long countFollowing(Integer userId);
    List<User> getFollowing(Integer userId);
    List<User> getFollowers(Integer userId);
    long getFollowerCount(Integer userId);
    boolean isFollowing(Integer followerId, Integer followeeId);
    void unfollow(Integer followerId, Integer followeeId);
    void follow(Integer followerId, Integer followeeId);
}
