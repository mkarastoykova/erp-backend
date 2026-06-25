package com.erp.crm.service;

import com.erp.common.dto.crm.CrmActivityDto;
import com.erp.common.enums.CrmActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CrmActivityService {
    Page<CrmActivityDto> search(CrmActivityType type, String q, Pageable pageable);
    CrmActivityDto findById(Long id);
    List<CrmActivityDto> findByContact(Long contactId);
    CrmActivityDto create(CrmActivityDto dto);
    CrmActivityDto update(Long id, CrmActivityDto dto);
    void delete(Long id);
}

