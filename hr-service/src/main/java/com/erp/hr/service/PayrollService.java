package com.erp.hr.service;

import com.erp.common.dto.PayrollRecordDto;
import com.erp.common.enums.PayrollStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PayrollService {

    PayrollRecordDto create(PayrollRecordDto dto);

    PayrollRecordDto update(Long id, PayrollRecordDto dto);

    PayrollRecordDto findById(Long id);

    Page<PayrollRecordDto> search(String month, PayrollStatus status, String department, Pageable pageable);

    List<PayrollRecordDto> findByEmployee(Long employeeId);

    List<PayrollRecordDto> findByMonth(String month);

    PayrollRecordDto process(Long id);

    PayrollRecordDto disburse(Long id);

    BigDecimal totalNetPayByMonth(String month);

    void delete(Long id);
}

