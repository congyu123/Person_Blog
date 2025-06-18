package com.cy.person_blog.service;

import com.cy.person_blog.entity.User;
import com.cy.person_blog.entity.UserFollow;
import com.cy.person_blog.repository.UserFollowRepository;
import com.cy.person_blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {
    @Autowired
    private UserFollowRepository ufRepo;
    @Autowired private UserRepository userRepo;

    @Transactional
    public void follow(Integer followerId, Integer followeeId) {
        if (!ufRepo.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            ufRepo.save(new UserFollow(followerId, followeeId));
            userRepo.incrementFollowerCount(followeeId);
        }
    }

    @Transactional
    public void unfollow(Integer followerId, Integer followeeId) {
        if (ufRepo.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            ufRepo.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
            userRepo.decrementFollowerCount(followeeId);
        }
    }

    public boolean isFollowing(Integer followerId, Integer followeeId) {
        return ufRepo.existsByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    public long getFollowerCount(Integer userId) {
        return ufRepo.countByFolloweeId(userId);
    }

    public List<User> getFollowers(Integer userId) {
        return ufRepo.findByFolloweeId(userId)
                .stream().map(uf -> userRepo.findById(uf.getFollowerId()).get())
                .collect(Collectors.toList());
    }

    public List<User> getFollowing(Integer userId) {
        return ufRepo.findByFollowerId(userId)
                .stream().map(uf -> userRepo.findById(uf.getFolloweeId()).get())
                .collect(Collectors.toList());
    }

    public long countFollowing(Integer userId) {
        return ufRepo.countByFollowerId(userId);
    }
}