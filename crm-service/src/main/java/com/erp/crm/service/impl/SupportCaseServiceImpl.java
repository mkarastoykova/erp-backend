package com.erp.crm.service.impl;

import com.erp.common.dto.crm.SupportCaseDto;
import com.erp.common.enums.CasePriority;
import com.erp.common.enums.CaseStatus;
import com.erp.common.entity.SupportCase;
import com.erp.crm.exception.ResourceNotFoundException;
import com.erp.crm.mapper.SupportCaseMapper;
import com.erp.crm.repository.CrmAccountRepository;
import com.erp.crm.repository.SupportCaseRepository;
import com.erp.crm.service.SupportCaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service @RequiredArgsConstructor @Slf4j @Transactional(readOnly = true)
public class SupportCaseServiceImpl implements SupportCaseService {

    private final SupportCaseRepository repo;
    private final CrmAccountRepository accountRepo;
    private final SupportCaseMapper mapper;

    @Override
    public Page<SupportCaseDto> search(CaseStatus status, CasePriority priority, String q, Pageable pageable) {
        return repo.search(status, priority, q, pageable).map(mapper::toDto);
    }

    @Override public SupportCaseDto findById(Long id) { return mapper.toDto(get(id)); }

    @Override @Transactional
    public SupportCaseDto create(SupportCaseDto dto) {
        SupportCase e = mapper.toEntity(dto);
        e.setCreatedAt(LocalDate.now());
        e.setUpdatedAt(LocalDate.now());
        resolveAccount(e, dto.getAccountId());
        return mapper.toDto(repo.save(e));
    }

    @Override @Transactional
    public SupportCaseDto update(Long id, SupportCaseDto dto) {
        SupportCase e = get(id);
        mapper.updateFromDto(dto, e);
        e.setUpdatedAt(LocalDate.now());
        resolveAccount(e, dto.getAccountId());
        return mapper.toDto(repo.save(e));
    }

    @Override @Transactional
    public SupportCaseDto updateStatus(Long id, CaseStatus status) {
        SupportCase e = get(id);
        e.setStatus(status);
        e.setUpdatedAt(LocalDate.now());
        return mapper.toDto(repo.save(e));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("SupportCase", "id", id);
        repo.deleteById(id);
    }

    private SupportCase get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SupportCase", "id", id));
    }

    private void resolveAccount(SupportCase e, Long accountId) {
        if (accountId != null) {
            var acc = accountRepo.findById(accountId)
                    .orElseThrow(() -> new ResourceNotFoundException("CrmAccount", "id", accountId));
            e.setAccount(acc);
            e.setAccountName(acc.getName());
        }
    }
}

