package com.cy.person_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

@EnableJpaRepositories(basePackages = "com.cy.person_blog.repository") // 显式启用扫描

public class PersonBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonBlogApplication.class, args);
    }

}
