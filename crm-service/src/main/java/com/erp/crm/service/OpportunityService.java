package com.erp.crm.service;

import com.erp.common.dto.crm.OpportunityDto;
import com.erp.common.enums.OpportunityStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OpportunityService {
    Page<OpportunityDto> search(OpportunityStage stage, String owner, String q, Pageable pageable);
    OpportunityDto findById(Long id);
    OpportunityDto create(OpportunityDto dto);
    OpportunityDto update(Long id, OpportunityDto dto);
    OpportunityDto updateStage(Long id, OpportunityStage stage);
    void delete(Long id);
}

