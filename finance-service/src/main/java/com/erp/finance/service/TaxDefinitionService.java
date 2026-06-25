package com.erp.finance.service;

import com.erp.common.dto.finance.TaxDefinitionDto;
import java.util.List;

public interface TaxDefinitionService {
    List<TaxDefinitionDto> findAll();
    TaxDefinitionDto findById(Long id);
    TaxDefinitionDto create(TaxDefinitionDto dto);
    TaxDefinitionDto update(Long id, TaxDefinitionDto dto);
    void delete(Long id);
}

