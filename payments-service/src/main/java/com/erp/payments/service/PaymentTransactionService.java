package com.erp.payments.service;

import com.erp.common.dto.payments.PaymentMethodStatsDto;
import com.erp.common.dto.payments.PaymentTransactionDto;
import com.erp.common.dto.payments.PaymentTrendDto;
import com.erp.common.enums.PaymentTxMethod;
import com.erp.common.enums.PaymentTxStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentTransactionService {

    Page<PaymentTransactionDto> search(PaymentTxStatus status, PaymentTxMethod method, String q, Pageable pageable);

    PaymentTransactionDto findById(Long id);

    PaymentTransactionDto findByCode(String code);

    PaymentTransactionDto create(PaymentTransactionDto dto);

    PaymentTransactionDto updateStatus(Long id, PaymentTxStatus status);

    /** Creates a mirror REFUNDED transaction for the given payment */
    PaymentTransactionDto refund(Long id);

    void delete(Long id);

    /** Analytics: total amount and count by payment method (COMPLETED only) */
    List<PaymentMethodStatsDto> getStatsByMethod();

    /** Analytics: weekly received vs outstanding for the last N weeks */
    List<PaymentTrendDto> getWeeklyTrend(int weeks);
}

