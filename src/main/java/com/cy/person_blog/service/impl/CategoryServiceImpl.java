// CategoryServiceImpl.java
package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.Category;
import com.cy.person_blog.repository.CategoryRepository;
import com.cy.person_blog.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public List<Category> findAllTree() {
        List<Category> all = categoryRepo.findAll();
        Map<Integer, Category> map = all.stream()
                .collect(Collectors.toMap(Category::getId, c -> c));
        List<Category> roots = new ArrayList<>();
        for (Category c : all) {
            if (c.getParentId() == null) {
                roots.add(c);
            } else {
                Category parent = map.get(c.getParentId());
                if (parent != null) {
                    parent.getChildren().add(c);
                }
            }
        }
        return roots;
    }
}
