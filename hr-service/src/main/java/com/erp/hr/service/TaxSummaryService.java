package com.erp.hr.service;

import com.erp.common.dto.TaxSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaxSummaryService {

    TaxSummaryDto create(TaxSummaryDto dto);

    TaxSummaryDto update(Long id, TaxSummaryDto dto);

    TaxSummaryDto findById(Long id);

    List<TaxSummaryDto> findByEmployee(Long employeeId);

    Page<TaxSummaryDto> search(Integer year, String department, Boolean w2Issued, Pageable pageable);

    TaxSummaryDto issueW2(Long id);

    void delete(Long id);
}

