package com.erp.crm.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.crm.CrmContactDto;
import com.erp.crm.service.CrmContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/contacts") @RequiredArgsConstructor
public class CrmContactController {

    private final CrmContactService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CrmContactDto>>> search(
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(owner, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CrmContactDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @GetMapping("/by-account/{accountId}")
    public ResponseEntity<ApiResponse<List<CrmContactDto>>> byAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByAccount(accountId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CrmContactDto>> create(@Valid @RequestBody CrmContactDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Contact created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CrmContactDto>> update(@PathVariable Long id,
                                                              @Valid @RequestBody CrmContactDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Contact updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Contact deleted", null));
    }
}

