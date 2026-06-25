package com.erp.finance.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.finance.TaxDefinitionDto;
import com.erp.finance.service.TaxDefinitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tax-definitions")
@RequiredArgsConstructor
public class TaxDefinitionController {

    private final TaxDefinitionService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaxDefinitionDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxDefinitionDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaxDefinitionDto>> create(@Valid @RequestBody TaxDefinitionDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tax definition created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxDefinitionDto>> update(@PathVariable Long id,
                                                                 @Valid @RequestBody TaxDefinitionDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Tax definition updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Tax definition deleted", null));
    }
}

