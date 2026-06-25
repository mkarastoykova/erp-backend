package com.erp.inventory.service;

import com.erp.common.dto.inventory.InventoryItemDto;
import com.erp.common.enums.InventoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryItemService {
    Page<InventoryItemDto> search(String category, InventoryStatus status, String q, Pageable pageable);
    InventoryItemDto findById(Long id);
    InventoryItemDto findBySku(String sku);
    InventoryItemDto create(InventoryItemDto dto);
    InventoryItemDto update(Long id, InventoryItemDto dto);
    /** Positive delta = stock in, negative delta = stock out */
    InventoryItemDto adjustStock(Long id, int delta, String reason);
    void delete(Long id);
    List<InventoryItemDto> getLowStockAlerts();
    List<InventoryItemDto> getOutOfStockItems();
    List<String> getCategories();
}

