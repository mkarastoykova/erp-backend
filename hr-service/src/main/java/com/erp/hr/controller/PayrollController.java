package com.erp.hr.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.PayrollRecordDto;
import com.erp.common.enums.PayrollStatus;
import com.erp.hr.service.PayrollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping
    public ResponseEntity<ApiResponse<PayrollRecordDto>> create(@Valid @RequestBody PayrollRecordDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Payroll record created", payrollService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PayrollRecordDto>> update(@PathVariable Long id,
                                                                 @Valid @RequestBody PayrollRecordDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Payroll record updated", payrollService.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PayrollRecordDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(payrollService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PayrollRecordDto>>> search(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) PayrollStatus status,
            @RequestParam(required = false) String department,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(payrollService.search(month, status, department, pageable)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<PayrollRecordDto>>> findByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.ok(payrollService.findByEmployee(employeeId)));
    }

    @GetMapping("/month/{month}")
    public ResponseEntity<ApiResponse<List<PayrollRecordDto>>> findByMonth(@PathVariable String month) {
        return ResponseEntity.ok(ApiResponse.ok(payrollService.findByMonth(month)));
    }

    @GetMapping("/month/{month}/total")
    public ResponseEntity<ApiResponse<BigDecimal>> totalByMonth(@PathVariable String month) {
        return ResponseEntity.ok(ApiResponse.ok(payrollService.totalNetPayByMonth(month)));
    }

    @PatchMapping("/{id}/process")
    public ResponseEntity<ApiResponse<PayrollRecordDto>> process(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Payroll processed", payrollService.process(id)));
    }

    @PatchMapping("/{id}/disburse")
    public ResponseEntity<ApiResponse<PayrollRecordDto>> disburse(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Payroll disbursed", payrollService.disburse(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        payrollService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Payroll record deleted", null));
    }
}

