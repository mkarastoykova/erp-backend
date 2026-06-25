package com.erp.crm.service;

import com.erp.common.dto.crm.CrmAccountDto;
import com.erp.common.enums.AccountType;
import com.erp.common.enums.CrmAccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrmAccountService {
    Page<CrmAccountDto> search(AccountType type, CrmAccountStatus status, String q, Pageable pageable);
    CrmAccountDto findById(Long id);
    CrmAccountDto create(CrmAccountDto dto);
    CrmAccountDto update(Long id, CrmAccountDto dto);
    void delete(Long id);
}

