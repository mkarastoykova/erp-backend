package com.erp.payments.repository;

import com.erp.common.enums.PaymentTxMethod;
import com.erp.common.enums.PaymentTxStatus;
import com.erp.common.entity.PaymentTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {

    Optional<PaymentTransaction> findByPaymentCode(String code);
    boolean existsByPaymentCode(String code);

    @Query("SELECT p FROM PaymentTransaction p WHERE " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:method IS NULL OR p.method = :method) AND " +
           "(:q IS NULL OR LOWER(p.payer) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(p.paymentCode) LIKE LOWER(CONCAT('%',:q,'%')) " +
           " OR LOWER(p.description) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<PaymentTransaction> search(@Param("status") PaymentTxStatus status,
                                     @Param("method") PaymentTxMethod method,
                                     @Param("q") String q,
                                     Pageable pageable);

    /** Aggregate: method, total, count — for analytics pie chart */
    @Query("SELECT p.method, SUM(p.amount), COUNT(p) FROM PaymentTransaction p " +
           "WHERE p.status = com.erp.common.enums.PaymentTxStatus.COMPLETED " +
           "GROUP BY p.method ORDER BY SUM(p.amount) DESC")
    List<Object[]> aggregateByMethod();

    /** All payments within a date range for trend calculation */
    List<PaymentTransaction> findByDateBetweenOrderByDateAsc(LocalDate from, LocalDate to);
}

