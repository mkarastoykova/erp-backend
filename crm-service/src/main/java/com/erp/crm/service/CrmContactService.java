package com.erp.crm.service;

import com.erp.common.dto.crm.CrmContactDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CrmContactService {
    Page<CrmContactDto> search(String owner, String q, Pageable pageable);
    CrmContactDto findById(Long id);
    List<CrmContactDto> findByAccount(Long accountId);
    CrmContactDto create(CrmContactDto dto);
    CrmContactDto update(Long id, CrmContactDto dto);
    void delete(Long id);
}

