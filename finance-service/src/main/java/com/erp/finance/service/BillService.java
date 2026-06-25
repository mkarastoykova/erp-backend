package com.erp.finance.service;

import com.erp.common.dto.finance.BillDto;
import com.erp.common.enums.CorrectionMode;
import com.erp.common.enums.InvoiceDocStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BillService {
    Page<BillDto> search(InvoiceDocStatus status, String q, Pageable pageable);
    BillDto findById(Long id);
    BillDto findByNumber(String number);
    BillDto create(BillDto dto);
    BillDto update(Long id, BillDto dto);
    BillDto advanceStatus(Long id);
    BillDto cancel(Long id);
    BillDto registerPayment(Long id);
    BillDto createCorrection(Long id, CorrectionMode mode);
    void delete(Long id);
}

