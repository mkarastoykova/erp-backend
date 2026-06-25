package com.erp.crm.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.crm.SupportCaseDto;
import com.erp.common.enums.CasePriority;
import com.erp.common.enums.CaseStatus;
import com.erp.crm.service.SupportCaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/cases") @RequiredArgsConstructor
public class SupportCaseController {

    private final SupportCaseService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SupportCaseDto>>> search(
            @RequestParam(required = false) CaseStatus status,
            @RequestParam(required = false) CasePriority priority,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(status, priority, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupportCaseDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SupportCaseDto>> create(@Valid @RequestBody SupportCaseDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Case created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupportCaseDto>> update(@PathVariable Long id,
                                                               @Valid @RequestBody SupportCaseDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Case updated", service.update(id, dto)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SupportCaseDto>> updateStatus(@PathVariable Long id,
                                                                      @RequestParam CaseStatus status) {
        return ResponseEntity.ok(ApiResponse.ok("Status updated", service.updateStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Case deleted", null));
    }
}

