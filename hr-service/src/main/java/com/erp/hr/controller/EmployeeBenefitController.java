package com.erp.hr.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.EmployeeBenefitDto;
import com.erp.common.enums.BenefitStatus;
import com.erp.common.enums.BenefitType;
import com.erp.hr.service.EmployeeBenefitService;
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
@RequestMapping("/benefits")
@RequiredArgsConstructor
public class EmployeeBenefitController {

    private final EmployeeBenefitService benefitService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeBenefitDto>> create(@Valid @RequestBody EmployeeBenefitDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Benefit created", benefitService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeBenefitDto>> update(@PathVariable Long id,
                                                                    @Valid @RequestBody EmployeeBenefitDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Benefit updated", benefitService.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeBenefitDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(benefitService.findById(id)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<EmployeeBenefitDto>>> findByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.ok(benefitService.findByEmployee(employeeId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmployeeBenefitDto>>> search(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) BenefitType type,
            @RequestParam(required = false) BenefitStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(benefitService.search(employeeId, type, status, pageable)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        benefitService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Benefit deleted", null));
    }
}

