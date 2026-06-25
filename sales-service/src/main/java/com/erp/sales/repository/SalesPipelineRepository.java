package com.erp.sales.repository;

import com.erp.common.entity.SalesPipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesPipelineRepository extends JpaRepository<SalesPipeline, String> {
}

