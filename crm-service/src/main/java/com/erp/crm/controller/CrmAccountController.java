package com.erp.crm.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.crm.CrmAccountDto;
import com.erp.common.enums.AccountType;
import com.erp.common.enums.CrmAccountStatus;
import com.erp.crm.service.CrmAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/accounts") @RequiredArgsConstructor
public class CrmAccountController {

    private final CrmAccountService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CrmAccountDto>>> search(
            @RequestParam(required = false) AccountType type,
            @RequestParam(required = false) CrmAccountStatus status,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(type, status, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CrmAccountDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CrmAccountDto>> create(@Valid @RequestBody CrmAccountDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Account created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CrmAccountDto>> update(@PathVariable Long id,
                                                              @Valid @RequestBody CrmAccountDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Account updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Account deleted", null));
    }
}

