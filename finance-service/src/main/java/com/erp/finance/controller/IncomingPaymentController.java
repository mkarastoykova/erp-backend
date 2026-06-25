package com.erp.finance.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.finance.IncomingPaymentDto;
import com.erp.common.enums.PaymentMatchStatus;
import com.erp.finance.service.IncomingPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/incoming-payments")
@RequiredArgsConstructor
public class IncomingPaymentController {

    private final IncomingPaymentService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<IncomingPaymentDto>>> search(
            @RequestParam(required = false) PaymentMatchStatus matchStatus,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(matchStatus, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomingPaymentDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<IncomingPaymentDto>> create(@Valid @RequestBody IncomingPaymentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Incoming payment created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IncomingPaymentDto>> update(@PathVariable Long id,
                                                                   @Valid @RequestBody IncomingPaymentDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Incoming payment updated", service.update(id, dto)));
    }

    @PostMapping("/{id}/match")
    public ResponseEntity<ApiResponse<IncomingPaymentDto>> match(
            @PathVariable Long id,
            @RequestParam String docNumber,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.ok("Document matched", service.matchDocument(id, docNumber, amount)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Incoming payment deleted", null));
    }
}

