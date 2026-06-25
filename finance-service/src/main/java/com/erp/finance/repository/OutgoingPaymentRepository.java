package com.erp.finance.repository;

import com.erp.common.enums.PaymentMatchStatus;
import com.erp.common.entity.OutgoingPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OutgoingPaymentRepository extends JpaRepository<OutgoingPayment, Long> {
    Optional<OutgoingPayment> findByPaymentNumber(String paymentNumber);
    boolean existsByPaymentNumber(String paymentNumber);

    @Query("SELECT p FROM OutgoingPayment p WHERE " +
           "(:matchStatus IS NULL OR p.matchStatus = :matchStatus) AND " +
           "(:q IS NULL OR LOWER(p.paymentNumber) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "  OR LOWER(p.partner) LIKE LOWER(CONCAT('%',:q,'%')) " +
           "  OR LOWER(p.base) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<OutgoingPayment> search(@Param("matchStatus") PaymentMatchStatus matchStatus,
                                  @Param("q") String q,
                                  Pageable pageable);
}

