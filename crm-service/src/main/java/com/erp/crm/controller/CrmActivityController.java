package com.erp.crm.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.crm.CrmActivityDto;
import com.erp.common.enums.CrmActivityType;
import com.erp.crm.service.CrmActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/activities") @RequiredArgsConstructor
public class CrmActivityController {

    private final CrmActivityService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CrmActivityDto>>> search(
            @RequestParam(required = false) CrmActivityType type,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "date") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(type, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CrmActivityDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @GetMapping("/by-contact/{contactId}")
    public ResponseEntity<ApiResponse<List<CrmActivityDto>>> byContact(@PathVariable Long contactId) {
        return ResponseEntity.ok(ApiResponse.ok(service.findByContact(contactId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CrmActivityDto>> create(@Valid @RequestBody CrmActivityDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Activity logged", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CrmActivityDto>> update(@PathVariable Long id,
                                                               @Valid @RequestBody CrmActivityDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Activity updated", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Activity deleted", null));
    }
}

