package com.erp.inventory.repository;

import com.erp.common.enums.InventoryStatus;
import com.erp.common.entity.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findBySku(String sku);
    Optional<InventoryItem> findByItemCode(String itemCode);
    boolean existsBySku(String sku);
    boolean existsByItemCode(String itemCode);

    List<InventoryItem> findByStatusIn(List<InventoryStatus> statuses);

    @Query("SELECT i FROM InventoryItem i WHERE " +
           "(:category IS NULL OR i.category = :category) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:q IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(i.sku) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<InventoryItem> search(@Param("category") String category,
                                @Param("status") InventoryStatus status,
                                @Param("q") String q,
                                Pageable pageable);
}

