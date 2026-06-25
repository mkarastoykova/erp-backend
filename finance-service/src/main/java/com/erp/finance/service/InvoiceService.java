package com.erp.finance.service;

import com.erp.common.dto.finance.InvoiceDto;
import com.erp.common.enums.InvoiceDocStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvoiceService {
    Page<InvoiceDto> search(InvoiceDocStatus status, String q, Pageable pageable);
    InvoiceDto findById(Long id);
    InvoiceDto findByNumber(String number);
    InvoiceDto create(InvoiceDto dto);
    InvoiceDto update(Long id, InvoiceDto dto);
    InvoiceDto advanceStatus(Long id);
    InvoiceDto cancel(Long id);
    InvoiceDto registerPayment(Long id);
    InvoiceDto markShipmentCreated(Long id);
    void delete(Long id);
}

