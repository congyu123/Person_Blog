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
    public List<Category> listAllCategories() {
        return categoryRepo.findAll();
    }
}
