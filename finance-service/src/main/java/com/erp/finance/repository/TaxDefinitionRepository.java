package com.erp.finance.repository;

import com.erp.common.entity.TaxDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaxDefinitionRepository extends JpaRepository<TaxDefinition, Long> {
    Optional<TaxDefinition> findByTaxCode(String taxCode);
    boolean existsByTaxCode(String taxCode);
}

