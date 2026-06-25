package com.erp.catalog.repository;
import com.erp.common.entity.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SkuRepository extends JpaRepository<Sku, String> {
}
