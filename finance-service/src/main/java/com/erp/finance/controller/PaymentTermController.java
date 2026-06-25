package com.erp.finance.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.finance.PaymentTermDefDto;
import com.erp.finance.service.PaymentTermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-terms")
@RequiredArgsConstructor
public class PaymentTermController {

    private final PaymentTermService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentTermDefDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentTermDefDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentTermDefDto>> create(@Valid @RequestBody PaymentTermDefDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Payment term created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentTermDefDto>> update(@PathVariable Long id,
                                                                  @Valid @RequestBody PaymentTermDefDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Payment term updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Payment term deleted", null));
    }
}

