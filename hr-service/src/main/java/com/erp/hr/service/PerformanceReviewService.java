package com.erp.hr.service;

import com.erp.common.dto.PerformanceReviewDto;
import com.erp.common.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PerformanceReviewService {

    PerformanceReviewDto create(PerformanceReviewDto dto);

    PerformanceReviewDto update(Long id, PerformanceReviewDto dto);

    PerformanceReviewDto findById(Long id);

    List<PerformanceReviewDto> findByEmployee(Long employeeId);

    Page<PerformanceReviewDto> search(Long employeeId, ReviewStatus status, String period, String department, Pageable pageable);

    PerformanceReviewDto complete(Long id);

    void delete(Long id);
}

