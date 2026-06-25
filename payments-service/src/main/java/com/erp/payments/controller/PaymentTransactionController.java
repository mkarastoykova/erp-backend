package com.erp.payments.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.payments.PaymentTransactionDto;
import com.erp.common.enums.PaymentTxMethod;
import com.erp.common.enums.PaymentTxStatus;
import com.erp.payments.service.PaymentTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class PaymentTransactionController {

    private final PaymentTransactionService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PaymentTransactionDto>>> search(
            @RequestParam(required = false) PaymentTxStatus status,
            @RequestParam(required = false) PaymentTxMethod method,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "date") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(status, method, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentTransactionDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<PaymentTransactionDto>> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByCode(code)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentTransactionDto>> create(@Valid @RequestBody PaymentTransactionDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Payment recorded", service.create(dto)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PaymentTransactionDto>> updateStatus(
            @PathVariable Long id,
            @RequestParam PaymentTxStatus status) {
        return ResponseEntity.ok(ApiResponse.ok("Status updated", service.updateStatus(id, status)));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<ApiResponse<PaymentTransactionDto>> refund(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Refund created", service.refund(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Payment deleted", null));
    }
}

