package com.erp.hr.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.EmployeeDto;
import com.erp.common.enums.EmployeeStatus;
import com.erp.hr.service.EmployeeService;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDto>> create(@Valid @RequestBody EmployeeDto dto) {
        EmployeeDto created = employeeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Employee created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDto>> update(@PathVariable Long id,
                                                            @Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Employee updated successfully", employeeService.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(employeeService.findById(id)));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<EmployeeDto>> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.ok(employeeService.findByCode(code)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<EmployeeDto>>> search(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(employeeService.search(department, status, q, pageable)));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> findByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(ApiResponse.ok(employeeService.findByDepartment(department)));
    }

    @GetMapping("/{id}/direct-reports")
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> findDirectReports(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(employeeService.findDirectReports(id)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@PathVariable Long id,
                                                           @RequestParam EmployeeStatus status) {
        employeeService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.ok("Status updated", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Employee deleted", null));
    }
}

