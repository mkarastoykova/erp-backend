package com.erp.inventory.dataloader;

import com.erp.common.entity.InventoryItem;
import com.erp.inventory.repository.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Loads the 10 inventory items from the frontend mock data.
 * Activate with: --spring.profiles.active=mock
 */
@Component
@Profile("mock")
@RequiredArgsConstructor
@Slf4j
public class InventoryMockDataLoader implements ApplicationRunner {

    private final InventoryItemRepository repo;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (repo.count() > 0) { log.info("Inventory mock data already loaded — skipping."); return; }
        log.info("Loading Inventory mock data…");

        repo.saveAll(List.of(
            item("P001", "Industrial Pump A200", "IPA-200", "Machinery",    45,  10,  1200.00, "Warehouse A"),
            item("P002", "Bolt Set M10 (100pc)", "BSM-010", "Hardware",      8,  50,    12.00, "Shelf B3"),
            item("P003", "Circuit Board CB-7",   "CBR-007", "Electronics",   0,  20,   450.00, "Warehouse B"),
            item("P004", "Safety Helmet XL",     "SHX-001", "Safety",      120,  30,    38.00, "Shelf C1"),
            item("P005", "Lubricant Oil 5L",     "LOL-005", "Consumables",  62,  20,    25.00, "Storage D"),
            item("P006", "Steel Pipe 2inch",     "STP-002", "Raw Materials",  5, 25,    85.00, "Yard 1"),
            item("P007", "Electric Motor 5HP",   "EMT-005", "Machinery",    18,   5,  2200.00, "Warehouse A"),
            item("P008", "Rubber Gasket Kit",    "RGK-001", "Hardware",    340, 100,     8.00, "Shelf B5"),
            item("P009", "PLC Controller S7",    "PLC-S07", "Electronics",   3,  10,  3800.00, "Warehouse B"),
            item("P010", "Work Gloves (pair)",   "WGP-001", "Safety",        0,  50,     6.00, "Shelf C2")
        ));

        log.info("Inventory mock data loaded: 10 items.");
    }

    private InventoryItem item(String code, String name, String sku, String category,
                                int qty, int reorder, double unitCost, String location) {
        InventoryItem i = InventoryItem.builder()
                .itemCode(code).name(name).sku(sku).category(category)
                .quantity(qty).reorderLevel(reorder)
                .unitCost(BigDecimal.valueOf(unitCost))
                .location(location)
                .build();
        i.recalcStatus();
        return i;
    }
}

