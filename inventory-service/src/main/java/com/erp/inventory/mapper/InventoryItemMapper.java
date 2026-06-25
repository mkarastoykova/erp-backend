package com.erp.inventory.mapper;

import com.erp.common.dto.inventory.InventoryItemDto;
import com.erp.common.entity.InventoryItem;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InventoryItemMapper {

    @Mapping(target = "totalValue", expression = "java(calcTotalValue(item))")
    InventoryItemDto toDto(InventoryItem item);

    InventoryItem toEntity(InventoryItemDto dto);

    void updateFromDto(InventoryItemDto dto, @MappingTarget InventoryItem item);

    default BigDecimal calcTotalValue(InventoryItem item) {
        if (item.getQuantity() == null || item.getUnitCost() == null) return BigDecimal.ZERO;
        return item.getUnitCost().multiply(BigDecimal.valueOf(item.getQuantity()));
    }
}

