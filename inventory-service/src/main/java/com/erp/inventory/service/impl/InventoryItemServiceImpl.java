package com.erp.inventory.service.impl;

import com.erp.common.dto.inventory.InventoryItemDto;
import com.erp.common.enums.InventoryStatus;
import com.erp.common.entity.InventoryItem;
import com.erp.inventory.exception.DuplicateResourceException;
import com.erp.inventory.exception.ResourceNotFoundException;
import com.erp.inventory.mapper.InventoryItemMapper;
import com.erp.inventory.repository.InventoryItemRepository;
import com.erp.inventory.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
@Transactional(readOnly = true)
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository repo;
    private final InventoryItemMapper mapper;

    @Override
    public Page<InventoryItemDto> search(String category, InventoryStatus status, String q, Pageable pageable) {
        String cat = (category != null && !category.isBlank()) ? category : null;
        String query = (q != null && !q.isBlank()) ? q : null;
        return repo.search(cat, status, query, pageable).map(mapper::toDto);
    }

    @Override
    public InventoryItemDto findById(Long id) {
        return mapper.toDto(get(id));
    }

    @Override
    public InventoryItemDto findBySku(String sku) {
        return mapper.toDto(repo.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", "sku", sku)));
    }

    @Override @Transactional
    public InventoryItemDto create(InventoryItemDto dto) {
        if (repo.existsBySku(dto.getSku()))
            throw new DuplicateResourceException("InventoryItem", "sku", dto.getSku());
        if (repo.existsByItemCode(dto.getItemCode()))
            throw new DuplicateResourceException("InventoryItem", "itemCode", dto.getItemCode());
        InventoryItem item = mapper.toEntity(dto);
        item.recalcStatus();
        InventoryItem saved = repo.save(item);
        log.info("Created inventory item [{}] sku={}", saved.getId(), saved.getSku());
        return mapper.toDto(saved);
    }

    @Override @Transactional
    public InventoryItemDto update(Long id, InventoryItemDto dto) {
        InventoryItem item = get(id);
        // Check SKU uniqueness only if it changed
        if (!item.getSku().equalsIgnoreCase(dto.getSku()) && repo.existsBySku(dto.getSku()))
            throw new DuplicateResourceException("InventoryItem", "sku", dto.getSku());
        mapper.updateFromDto(dto, item);
        item.recalcStatus();
        return mapper.toDto(repo.save(item));
    }

    @Override @Transactional
    public InventoryItemDto adjustStock(Long id, int delta, String reason) {
        InventoryItem item = get(id);
        int newQty = item.getQuantity() + delta;
        if (newQty < 0)
            throw new IllegalArgumentException(
                    "Stock adjustment would result in negative quantity (%d + %d = %d)".formatted(item.getQuantity(), delta, newQty));
        item.setQuantity(newQty);
        item.recalcStatus();
        log.info("Stock adjusted for item {} ({}): delta={}, reason={}, newQty={}", item.getSku(), item.getName(), delta, reason, newQty);
        return mapper.toDto(repo.save(item));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("InventoryItem", "id", id);
        repo.deleteById(id);
    }

    @Override
    public List<InventoryItemDto> getLowStockAlerts() {
        return repo.findByStatusIn(List.of(InventoryStatus.LOW_STOCK))
                .stream().map(mapper::toDto).toList();
    }

    @Override
    public List<InventoryItemDto> getOutOfStockItems() {
        return repo.findByStatusIn(List.of(InventoryStatus.OUT_OF_STOCK))
                .stream().map(mapper::toDto).toList();
    }

    @Override
    public List<String> getCategories() {
        return repo.findAll().stream()
                .map(InventoryItem::getCategory)
                .distinct().sorted().toList();
    }

    private InventoryItem get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("InventoryItem", "id", id));
    }
}

