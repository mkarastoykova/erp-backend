package com.erp.finance.repository;

import com.erp.common.enums.InvoiceDocStatus;
import com.erp.common.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    boolean existsByInvoiceNumber(String invoiceNumber);

    @Query("SELECT i FROM Invoice i WHERE " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:q IS NULL OR LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "  OR LOWER(i.customer) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "  OR LOWER(i.contract) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<Invoice> search(@Param("status") InvoiceDocStatus status,
                         @Param("q") String q,
                         Pageable pageable);
}

