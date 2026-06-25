package com.erp.hr.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.TaxSummaryDto;
import com.erp.hr.service.TaxSummaryService;
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
@RequestMapping("/tax-summaries")
@RequiredArgsConstructor
public class TaxSummaryController {

    private final TaxSummaryService taxSummaryService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaxSummaryDto>> create(@Valid @RequestBody TaxSummaryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tax summary created", taxSummaryService.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxSummaryDto>> update(@PathVariable Long id,
                                                              @Valid @RequestBody TaxSummaryDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Tax summary updated", taxSummaryService.update(id, dto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxSummaryDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(taxSummaryService.findById(id)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<TaxSummaryDto>>> findByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(ApiResponse.ok(taxSummaryService.findByEmployee(employeeId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaxSummaryDto>>> search(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Boolean w2Issued,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(taxSummaryService.search(year, department, w2Issued, pageable)));
    }

    @PatchMapping("/{id}/issue-w2")
    public ResponseEntity<ApiResponse<TaxSummaryDto>> issueW2(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("W-2 issued", taxSummaryService.issueW2(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        taxSummaryService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Tax summary deleted", null));
    }
}

