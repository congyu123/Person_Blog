package com.cy.person_blog.repository;

import com.cy.person_blog.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Integer> {
    List<Report> findByStatus(Report.ReportStatus status);
}