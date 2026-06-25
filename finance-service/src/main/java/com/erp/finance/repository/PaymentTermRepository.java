package com.erp.finance.repository;

import com.erp.common.entity.PaymentTermDef;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentTermRepository extends JpaRepository<PaymentTermDef, Long> {
    Optional<PaymentTermDef> findByTermCode(String termCode);
    boolean existsByTermCode(String termCode);
}

