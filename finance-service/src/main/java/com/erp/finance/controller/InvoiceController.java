package com.erp.finance.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.finance.InvoiceDto;
import com.erp.common.enums.InvoiceDocStatus;
import com.erp.finance.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<InvoiceDto>>> search(
            @RequestParam(required = false) InvoiceDocStatus status,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(status, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<ApiResponse<InvoiceDto>> findByNumber(@PathVariable String number) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByNumber(number)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceDto>> create(@Valid @RequestBody InvoiceDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Invoice created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceDto>> update(@PathVariable Long id,
                                                           @Valid @RequestBody InvoiceDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Invoice updated", service.update(id, dto)));
    }

    @PatchMapping("/{id}/advance-status")
    public ResponseEntity<ApiResponse<InvoiceDto>> advanceStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Status advanced", service.advanceStatus(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<InvoiceDto>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Invoice canceled", service.cancel(id)));
    }

    @PostMapping("/{id}/register-payment")
    public ResponseEntity<ApiResponse<InvoiceDto>> registerPayment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Payment registered", service.registerPayment(id)));
    }

    @PostMapping("/{id}/shipment")
    public ResponseEntity<ApiResponse<InvoiceDto>> createShipment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Shipment created", service.markShipmentCreated(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Invoice deleted", null));
    }
}

