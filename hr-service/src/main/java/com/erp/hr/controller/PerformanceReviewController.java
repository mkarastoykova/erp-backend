package com.erp.hr.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.PerformanceReviewDto;
import com.erp.common.enums.ReviewStatus;
import com.erp.hr.service.PerformanceReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/performance-reviews")
@RequiredArgsConstructor
public class PerformanceReviewController {

    private final PerformanceReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<PerformanceReviewDto>> create(@Valid @RequestBody PerformanceReviewDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Performance review created", reviewService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PerformanceReviewDto>> update(@PathVariable Long id,
                                                                     @Valid @RequestBody PerformanceReviewDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Performance review updated", reviewService.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PerformanceReviewDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(reviewService.findById(id)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<PerformanceReviewDto>>> findByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.ok(reviewService.findByEmployee(employeeId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PerformanceReviewDto>>> search(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) ReviewStatus status,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String department,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                reviewService.search(employeeId, status, period, department, pageable)));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<PerformanceReviewDto>> complete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Performance review completed", reviewService.complete(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Performance review deleted", null));
    }
}

