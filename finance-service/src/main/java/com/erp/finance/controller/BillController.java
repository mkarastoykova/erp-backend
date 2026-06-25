package com.erp.finance.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.finance.BillDto;
import com.erp.common.enums.CorrectionMode;
import com.erp.common.enums.InvoiceDocStatus;
import com.erp.finance.service.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BillDto>>> search(
            @RequestParam(required = false) InvoiceDocStatus status,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(status, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BillDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<ApiResponse<BillDto>> findByNumber(@PathVariable String number) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByNumber(number)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BillDto>> create(@Valid @RequestBody BillDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Bill created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BillDto>> update(@PathVariable Long id,
                                                        @Valid @RequestBody BillDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Bill updated", service.update(id, dto)));
    }

    @PatchMapping("/{id}/advance-status")
    public ResponseEntity<ApiResponse<BillDto>> advanceStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Status advanced", service.advanceStatus(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BillDto>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Bill canceled", service.cancel(id)));
    }

    @PostMapping("/{id}/register-payment")
    public ResponseEntity<ApiResponse<BillDto>> registerPayment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Payment registered", service.registerPayment(id)));
    }

    @PostMapping("/{id}/correction")
    public ResponseEntity<ApiResponse<BillDto>> createCorrection(
            @PathVariable Long id,
            @RequestParam(defaultValue = "REPLACEMENT") CorrectionMode mode) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Correction created", service.createCorrection(id, mode)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Bill deleted", null));
    }
}

