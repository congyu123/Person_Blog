// CategoryRepository.java
package com.cy.person_blog.repository;

import com.cy.person_blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findAll();
}
