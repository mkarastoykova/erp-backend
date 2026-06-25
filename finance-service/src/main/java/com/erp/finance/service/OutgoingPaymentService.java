package com.erp.finance.service;

import com.erp.common.dto.finance.OutgoingPaymentDto;
import com.erp.common.enums.PaymentMatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutgoingPaymentService {
    Page<OutgoingPaymentDto> search(PaymentMatchStatus matchStatus, String q, Pageable pageable);
    OutgoingPaymentDto findById(Long id);
    OutgoingPaymentDto create(OutgoingPaymentDto dto);
    OutgoingPaymentDto update(Long id, OutgoingPaymentDto dto);
    OutgoingPaymentDto matchDocument(Long id, String docNumber, java.math.BigDecimal amount);
    void delete(Long id);
}

