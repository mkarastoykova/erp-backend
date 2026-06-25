package com.erp.finance.service.impl;

import com.erp.common.dto.finance.TaxDefinitionDto;
import com.erp.finance.exception.DuplicateResourceException;
import com.erp.finance.exception.ResourceNotFoundException;
import com.erp.finance.mapper.TaxDefinitionMapper;
import com.erp.finance.repository.TaxDefinitionRepository;
import com.erp.finance.service.TaxDefinitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
@Transactional(readOnly = true)
public class TaxDefinitionServiceImpl implements TaxDefinitionService {

    private final TaxDefinitionRepository repo;
    private final TaxDefinitionMapper mapper;

    @Override public List<TaxDefinitionDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override public TaxDefinitionDto findById(Long id) {
        return mapper.toDto(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaxDefinition", "id", id)));
    }

    @Override @Transactional
    public TaxDefinitionDto create(TaxDefinitionDto dto) {
        if (repo.existsByTaxCode(dto.getTaxCode()))
            throw new DuplicateResourceException("TaxDefinition", "taxCode", dto.getTaxCode());
        return mapper.toDto(repo.save(mapper.toEntity(dto)));
    }

    @Override @Transactional
    public TaxDefinitionDto update(Long id, TaxDefinitionDto dto) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaxDefinition", "id", id));
        mapper.updateFromDto(dto, entity);
        return mapper.toDto(repo.save(entity));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("TaxDefinition", "id", id);
        repo.deleteById(id);
    }
}

