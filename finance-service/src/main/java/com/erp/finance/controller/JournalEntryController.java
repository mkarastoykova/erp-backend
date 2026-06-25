package com.erp.finance.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.finance.JournalEntryDto;
import com.erp.finance.service.JournalEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/journal-entries")
@RequiredArgsConstructor
public class JournalEntryController {

    private final JournalEntryService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<JournalEntryDto>>> search(
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "date") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(account, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JournalEntryDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<JournalEntryDto>> create(@Valid @RequestBody JournalEntryDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Journal entry created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JournalEntryDto>> update(@PathVariable Long id,
                                                                @Valid @RequestBody JournalEntryDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Journal entry updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Journal entry deleted", null));
    }
}

