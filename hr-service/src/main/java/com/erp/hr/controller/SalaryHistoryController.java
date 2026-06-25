package com.erp.hr.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.SalaryHistoryDto;
import com.erp.hr.service.SalaryHistoryService;
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
@RequestMapping("/salary-history")
@RequiredArgsConstructor
public class SalaryHistoryController {

    private final SalaryHistoryService salaryHistoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<SalaryHistoryDto>> create(@Valid @RequestBody SalaryHistoryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Salary history created", salaryHistoryService.create(dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalaryHistoryDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(salaryHistoryService.findById(id)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<SalaryHistoryDto>>> findByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.ok(salaryHistoryService.findByEmployee(employeeId)));
    }

    @GetMapping("/employee/{employeeId}/paged")
    public ResponseEntity<ApiResponse<Page<SalaryHistoryDto>>> findByEmployeePaged(
            @PathVariable Long employeeId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(salaryHistoryService.findByEmployeePaged(employeeId, pageable)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        salaryHistoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Salary history deleted", null));
    }
}

