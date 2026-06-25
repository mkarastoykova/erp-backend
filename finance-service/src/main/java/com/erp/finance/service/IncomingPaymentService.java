package com.erp.finance.service;

import com.erp.common.dto.finance.IncomingPaymentDto;
import com.erp.common.enums.PaymentMatchStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IncomingPaymentService {
    Page<IncomingPaymentDto> search(PaymentMatchStatus matchStatus, String q, Pageable pageable);
    IncomingPaymentDto findById(Long id);
    IncomingPaymentDto create(IncomingPaymentDto dto);
    IncomingPaymentDto update(Long id, IncomingPaymentDto dto);
    IncomingPaymentDto matchDocument(Long id, String docNumber, java.math.BigDecimal amount);
    void delete(Long id);
}

