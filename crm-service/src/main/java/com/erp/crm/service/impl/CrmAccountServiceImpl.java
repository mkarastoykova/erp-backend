package com.erp.crm.service.impl;

import com.erp.common.dto.crm.CrmAccountDto;
import com.erp.common.enums.AccountType;
import com.erp.common.enums.CrmAccountStatus;
import com.erp.crm.exception.DuplicateResourceException;
import com.erp.crm.exception.ResourceNotFoundException;
import com.erp.crm.mapper.CrmAccountMapper;
import com.erp.crm.repository.CrmAccountRepository;
import com.erp.crm.service.CrmAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Slf4j @Transactional(readOnly = true)
public class CrmAccountServiceImpl implements CrmAccountService {

    private final CrmAccountRepository repo;
    private final CrmAccountMapper mapper;

    @Override
    public Page<CrmAccountDto> search(AccountType type, CrmAccountStatus status, String q, Pageable pageable) {
        return repo.search(type, status, q, pageable).map(mapper::toDto);
    }

    @Override
    public CrmAccountDto findById(Long id) {
        return mapper.toDto(get(id));
    }

    @Override @Transactional
    public CrmAccountDto create(CrmAccountDto dto) {
        if (repo.existsByAccountCode(dto.getAccountCode()))
            throw new DuplicateResourceException("CrmAccount", "accountCode", dto.getAccountCode());
        return mapper.toDto(repo.save(mapper.toEntity(dto)));
    }

    @Override @Transactional
    public CrmAccountDto update(Long id, CrmAccountDto dto) {
        var e = get(id);
        mapper.updateFromDto(dto, e);
        return mapper.toDto(repo.save(e));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("CrmAccount", "id", id);
        repo.deleteById(id);
    }

    private com.erp.crm.entity.CrmAccount get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CrmAccount", "id", id));
    }
}

