package com.erp.crm.service.impl;

import com.erp.common.dto.crm.OpportunityDto;
import com.erp.common.enums.OpportunityStage;
import com.erp.common.entity.Opportunity;
import com.erp.crm.exception.ResourceNotFoundException;
import com.erp.crm.mapper.OpportunityMapper;
import com.erp.crm.repository.CrmAccountRepository;
import com.erp.crm.repository.CrmContactRepository;
import com.erp.crm.repository.OpportunityRepository;
import com.erp.crm.service.OpportunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Slf4j @Transactional(readOnly = true)
public class OpportunityServiceImpl implements OpportunityService {

    private final OpportunityRepository repo;
    private final CrmAccountRepository accountRepo;
    private final CrmContactRepository contactRepo;
    private final OpportunityMapper mapper;

    @Override
    public Page<OpportunityDto> search(OpportunityStage stage, String owner, String q, Pageable pageable) {
        return repo.search(stage, owner, q, pageable).map(mapper::toDto);
    }

    @Override public OpportunityDto findById(Long id) { return mapper.toDto(get(id)); }

    @Override @Transactional
    public OpportunityDto create(OpportunityDto dto) {
        Opportunity e = mapper.toEntity(dto);
        resolveRefs(e, dto);
        return mapper.toDto(repo.save(e));
    }

    @Override @Transactional
    public OpportunityDto update(Long id, OpportunityDto dto) {
        Opportunity e = get(id);
        mapper.updateFromDto(dto, e);
        resolveRefs(e, dto);
        return mapper.toDto(repo.save(e));
    }

    @Override @Transactional
    public OpportunityDto updateStage(Long id, OpportunityStage stage) {
        Opportunity e = get(id);
        e.setStage(stage);
        return mapper.toDto(repo.save(e));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("Opportunity", "id", id);
        repo.deleteById(id);
    }

    private Opportunity get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Opportunity", "id", id));
    }

    private void resolveRefs(Opportunity e, OpportunityDto dto) {
        if (dto.getAccountId() != null) {
            var acc = accountRepo.findById(dto.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("CrmAccount", "id", dto.getAccountId()));
            e.setAccount(acc);
            e.setAccountName(acc.getName());
        }
        if (dto.getContactId() != null) {
            var con = contactRepo.findById(dto.getContactId())
                    .orElseThrow(() -> new ResourceNotFoundException("CrmContact", "id", dto.getContactId()));
            e.setContact(con);
            e.setContactName(con.getFirstName() + " " + con.getLastName());
        }
    }
}

