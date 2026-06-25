package com.erp.sales.repository;

import com.erp.common.entity.SalesForecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesForecastRepository extends JpaRepository<SalesForecast, String> {
}

