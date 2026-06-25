package com.erp.hr.service.impl;

import com.erp.common.dto.PerformanceReviewDto;
import com.erp.common.entity.Employee;
import com.erp.common.entity.PerformanceReview;
import com.erp.common.enums.ReviewStatus;
import com.erp.hr.exception.ResourceNotFoundException;
import com.erp.hr.mapper.PerformanceReviewMapper;
import com.erp.hr.repository.EmployeeRepository;
import com.erp.hr.repository.PerformanceReviewRepository;
import com.erp.hr.service.PerformanceReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PerformanceReviewServiceImpl implements PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;
    private final PerformanceReviewMapper reviewMapper;

    @Override
    @Transactional
    public PerformanceReviewDto create(PerformanceReviewDto dto) {
        Employee employee = findEmployee(dto.getEmployeeId());
        PerformanceReview entity = reviewMapper.toEntity(dto);
        entity.setEmployee(employee);
        PerformanceReview saved = reviewRepository.save(entity);
        log.info("Created performance review [{}] for employee [{}] period {}", saved.getId(), employee.getId(), dto.getPeriod());
        return reviewMapper.toDto(saved);
    }

    @Override
    @Transactional
    public PerformanceReviewDto update(Long id, PerformanceReviewDto dto) {
        PerformanceReview entity = findEntityById(id);
        if (entity.getStatus() == ReviewStatus.COMPLETED) {
            throw new IllegalArgumentException("Cannot update a COMPLETED performance review");
        }
        reviewMapper.updateEntityFromDto(dto, entity);
        return reviewMapper.toDto(reviewRepository.save(entity));
    }

    @Override
    public PerformanceReviewDto findById(Long id) {
        return reviewMapper.toDto(findEntityById(id));
    }

    @Override
    public List<PerformanceReviewDto> findByEmployee(Long employeeId) {
        findEmployee(employeeId);
        return reviewRepository.findByEmployeeId(employeeId).stream()
                .map(reviewMapper::toDto)
                .toList();
    }

    @Override
    public Page<PerformanceReviewDto> search(Long employeeId, ReviewStatus status,
                                              String period, String department, Pageable pageable) {
        return reviewRepository.searchReviews(employeeId, status, period, department, pageable)
                .map(reviewMapper::toDto);
    }

    @Override
    @Transactional
    public PerformanceReviewDto complete(Long id) {
        PerformanceReview entity = findEntityById(id);
        if (entity.getStatus() == ReviewStatus.COMPLETED) {
            throw new IllegalArgumentException("Performance review is already COMPLETED");
        }
        if (entity.getRating() == null || entity.getRating() == 0) {
            throw new IllegalArgumentException("A rating must be set before completing a review");
        }
        entity.setStatus(ReviewStatus.COMPLETED);
        log.info("Completed performance review [{}]", id);
        return reviewMapper.toDto(reviewRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PerformanceReview entity = findEntityById(id);
        reviewRepository.delete(entity);
    }

    private PerformanceReview findEntityById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PerformanceReview", "id", id));
    }

    private Employee findEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
    }
}

