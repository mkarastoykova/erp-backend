package com.erp.crm.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.crm.OpportunityDto;
import com.erp.common.enums.OpportunityStage;
import com.erp.crm.service.OpportunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/opportunities") @RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OpportunityDto>>> search(
            @RequestParam(required = false) OpportunityStage stage,
            @RequestParam(required = false) String owner,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "closeDate") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(stage, owner, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OpportunityDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OpportunityDto>> create(@Valid @RequestBody OpportunityDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Opportunity created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OpportunityDto>> update(@PathVariable Long id,
                                                               @Valid @RequestBody OpportunityDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Opportunity updated", service.update(id, dto)));
    }

    @PatchMapping("/{id}/stage")
    public ResponseEntity<ApiResponse<OpportunityDto>> updateStage(@PathVariable Long id,
                                                                     @RequestParam OpportunityStage stage) {
        return ResponseEntity.ok(ApiResponse.ok("Stage updated", service.updateStage(id, stage)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Opportunity deleted", null));
    }
}

