package com.cy.person_blog.service.impl;

import com.cy.person_blog.entity.Report;
import com.cy.person_blog.repository.ReportRepository;
import com.cy.person_blog.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportRepository reportRepo;

    @Override
    @Transactional
    public void addReport(Report report) {
        reportRepo.save(report);
    }
}
