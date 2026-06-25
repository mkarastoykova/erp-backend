package com.erp.finance.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.finance.OutgoingPaymentDto;
import com.erp.common.enums.PaymentMatchStatus;
import com.erp.finance.service.OutgoingPaymentService;
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
@RequestMapping("/outgoing-payments")
@RequiredArgsConstructor
public class OutgoingPaymentController {

    private final OutgoingPaymentService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OutgoingPaymentDto>>> search(
            @RequestParam(required = false) PaymentMatchStatus matchStatus,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(matchStatus, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OutgoingPaymentDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OutgoingPaymentDto>> create(@Valid @RequestBody OutgoingPaymentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Outgoing payment created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OutgoingPaymentDto>> update(@PathVariable Long id,
                                                                   @Valid @RequestBody OutgoingPaymentDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Outgoing payment updated", service.update(id, dto)));
    }

    @PostMapping("/{id}/match")
    public ResponseEntity<ApiResponse<OutgoingPaymentDto>> match(
            @PathVariable Long id,
            @RequestParam String docNumber,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.ok("Document matched", service.matchDocument(id, docNumber, amount)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Outgoing payment deleted", null));
    }
}

