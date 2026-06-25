package com.erp.finance.service;

import com.erp.common.dto.finance.PaymentTermDefDto;
import java.util.List;

public interface PaymentTermService {
    List<PaymentTermDefDto> findAll();
    PaymentTermDefDto findById(Long id);
    PaymentTermDefDto create(PaymentTermDefDto dto);
    PaymentTermDefDto update(Long id, PaymentTermDefDto dto);
    void delete(Long id);
}

