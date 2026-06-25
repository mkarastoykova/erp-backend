package com.erp.hr.service;

import com.erp.common.dto.SalaryHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalaryHistoryService {

    SalaryHistoryDto create(SalaryHistoryDto dto);

    SalaryHistoryDto findById(Long id);

    List<SalaryHistoryDto> findByEmployee(Long employeeId);

    Page<SalaryHistoryDto> findByEmployeePaged(Long employeeId, Pageable pageable);

    void delete(Long id);
}

