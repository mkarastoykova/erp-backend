package com.erp.inventory.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.inventory.InventoryItemDto;
import com.erp.common.enums.InventoryStatus;
import com.erp.inventory.service.InventoryItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService service;

    /** Search with optional category, status and free-text filters */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<InventoryItemDto>>> search(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) InventoryStatus status,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.search(category, status, q, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ApiResponse<InventoryItemDto>> findBySku(@PathVariable String sku) {
        return ResponseEntity.ok(ApiResponse.ok(service.findBySku(sku)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryItemDto>> create(@Valid @RequestBody InventoryItemDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Inventory item created", service.create(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InventoryItemDto>> update(@PathVariable Long id,
                                                                 @Valid @RequestBody InventoryItemDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Inventory item updated", service.update(id, dto)));
    }

    /**
     * Adjust stock quantity.
     * @param delta  Positive = stock in, negative = stock out.
     * @param reason Optional reason / reference (e.g. "Purchase Order PO-001")
     */
    @PatchMapping("/{id}/adjust-stock")
    public ResponseEntity<ApiResponse<InventoryItemDto>> adjustStock(
            @PathVariable Long id,
            @RequestParam @NotNull int delta,
            @RequestParam(required = false, defaultValue = "") String reason) {
        return ResponseEntity.ok(ApiResponse.ok("Stock adjusted", service.adjustStock(id, delta, reason)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Inventory item deleted", null));
    }

    /** Returns items whose stock is at or below the reorder level */
    @GetMapping("/alerts/low-stock")
    public ResponseEntity<ApiResponse<List<InventoryItemDto>>> lowStock() {
        return ResponseEntity.ok(ApiResponse.ok(service.getLowStockAlerts()));
    }

    /** Returns items with zero stock */
    @GetMapping("/alerts/out-of-stock")
    public ResponseEntity<ApiResponse<List<InventoryItemDto>>> outOfStock() {
        return ResponseEntity.ok(ApiResponse.ok(service.getOutOfStockItems()));
    }

    /** Returns distinct categories currently in the catalogue */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<String>>> categories() {
        return ResponseEntity.ok(ApiResponse.ok(service.getCategories()));
    }
}

