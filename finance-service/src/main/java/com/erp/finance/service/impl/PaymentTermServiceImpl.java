package com.erp.finance.service.impl;

import com.erp.common.dto.finance.PaymentTermDefDto;
import com.erp.finance.exception.DuplicateResourceException;
import com.erp.finance.exception.ResourceNotFoundException;
import com.erp.finance.mapper.PaymentTermMapper;
import com.erp.finance.repository.PaymentTermRepository;
import com.erp.finance.service.PaymentTermService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
@Transactional(readOnly = true)
public class PaymentTermServiceImpl implements PaymentTermService {

    private final PaymentTermRepository repo;
    private final PaymentTermMapper mapper;

    @Override public List<PaymentTermDefDto> findAll() {
        return repo.findAll().stream().map(mapper::toDto).toList();
    }

    @Override public PaymentTermDefDto findById(Long id) {
        return mapper.toDto(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentTermDef", "id", id)));
    }

    @Override @Transactional
    public PaymentTermDefDto create(PaymentTermDefDto dto) {
        if (repo.existsByTermCode(dto.getTermCode()))
            throw new DuplicateResourceException("PaymentTermDef", "termCode", dto.getTermCode());
        return mapper.toDto(repo.save(mapper.toEntity(dto)));
    }

    @Override @Transactional
    public PaymentTermDefDto update(Long id, PaymentTermDefDto dto) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PaymentTermDef", "id", id));
        mapper.updateFromDto(dto, entity);
        return mapper.toDto(repo.save(entity));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("PaymentTermDef", "id", id);
        repo.deleteById(id);
    }
}

