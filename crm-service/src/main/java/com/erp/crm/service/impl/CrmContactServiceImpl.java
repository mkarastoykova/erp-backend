package com.erp.crm.service.impl;

import com.erp.common.dto.crm.CrmContactDto;
import com.erp.common.entity.CrmContact;
import com.erp.crm.exception.DuplicateResourceException;
import com.erp.crm.exception.ResourceNotFoundException;
import com.erp.crm.mapper.CrmContactMapper;
import com.erp.crm.repository.CrmAccountRepository;
import com.erp.crm.repository.CrmContactRepository;
import com.erp.crm.service.CrmContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Slf4j @Transactional(readOnly = true)
public class CrmContactServiceImpl implements CrmContactService {

    private final CrmContactRepository repo;
    private final CrmAccountRepository accountRepo;
    private final CrmContactMapper mapper;

    @Override
    public Page<CrmContactDto> search(String owner, String q, Pageable pageable) {
        return repo.search(owner, q, pageable).map(mapper::toDto);
    }

    @Override public CrmContactDto findById(Long id) { return mapper.toDto(get(id)); }

    @Override
    public List<CrmContactDto> findByAccount(Long accountId) {
        return repo.findByAccount_Id(accountId).stream().map(mapper::toDto).toList();
    }

    @Override @Transactional
    public CrmContactDto create(CrmContactDto dto) {
        if (repo.existsByContactCode(dto.getContactCode()))
            throw new DuplicateResourceException("CrmContact", "contactCode", dto.getContactCode());
        if (repo.existsByEmail(dto.getEmail()))
            throw new DuplicateResourceException("CrmContact", "email", dto.getEmail());
        CrmContact entity = mapper.toEntity(dto);
        resolveAccount(entity, dto.getAccountId());
        return mapper.toDto(repo.save(entity));
    }

    @Override @Transactional
    public CrmContactDto update(Long id, CrmContactDto dto) {
        CrmContact entity = get(id);
        if (!entity.getEmail().equalsIgnoreCase(dto.getEmail()) && repo.existsByEmail(dto.getEmail()))
            throw new DuplicateResourceException("CrmContact", "email", dto.getEmail());
        mapper.updateFromDto(dto, entity);
        resolveAccount(entity, dto.getAccountId());
        return mapper.toDto(repo.save(entity));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("CrmContact", "id", id);
        repo.deleteById(id);
    }

    private CrmContact get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CrmContact", "id", id));
    }

    private void resolveAccount(CrmContact c, Long accountId) {
        if (accountId != null) {
            var acc = accountRepo.findById(accountId)
                    .orElseThrow(() -> new ResourceNotFoundException("CrmAccount", "id", accountId));
            c.setAccount(acc);
            c.setCompany(acc.getName());
        }
    }
}

