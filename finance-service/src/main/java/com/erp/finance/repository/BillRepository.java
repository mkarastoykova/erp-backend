package com.erp.finance.repository;

import com.erp.common.enums.InvoiceDocStatus;
import com.erp.common.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBillNumber(String billNumber);
    boolean existsByBillNumber(String billNumber);
    List<Bill> findByOriginalBillNumber(String originalBillNumber);

    @Query("SELECT b FROM Bill b WHERE " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:q IS NULL OR LOWER(b.billNumber) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "  OR LOWER(b.vendor) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "  OR LOWER(b.contract) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<Bill> search(@Param("status") InvoiceDocStatus status,
                      @Param("q") String q,
                      Pageable pageable);
}

