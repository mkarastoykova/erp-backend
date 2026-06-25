package com.erp.crm.service;

import com.erp.common.dto.crm.SupportCaseDto;
import com.erp.common.enums.CasePriority;
import com.erp.common.enums.CaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupportCaseService {
    Page<SupportCaseDto> search(CaseStatus status, CasePriority priority, String q, Pageable pageable);
    SupportCaseDto findById(Long id);
    SupportCaseDto create(SupportCaseDto dto);
    SupportCaseDto update(Long id, SupportCaseDto dto);
    SupportCaseDto updateStatus(Long id, CaseStatus status);
    void delete(Long id);
}

