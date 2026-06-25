package com.erp.crm.service.impl;

import com.erp.common.dto.crm.CrmActivityDto;
import com.erp.common.enums.CrmActivityType;
import com.erp.common.entity.CrmActivity;
import com.erp.crm.exception.ResourceNotFoundException;
import com.erp.crm.mapper.CrmActivityMapper;
import com.erp.crm.repository.CrmActivityRepository;
import com.erp.crm.repository.CrmContactRepository;
import com.erp.crm.service.CrmActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor @Slf4j @Transactional(readOnly = true)
public class CrmActivityServiceImpl implements CrmActivityService {

    private final CrmActivityRepository repo;
    private final CrmContactRepository contactRepo;
    private final CrmActivityMapper mapper;

    @Override
    public Page<CrmActivityDto> search(CrmActivityType type, String q, Pageable pageable) {
        return repo.search(type, q, pageable).map(mapper::toDto);
    }

    @Override public CrmActivityDto findById(Long id) { return mapper.toDto(get(id)); }

    @Override
    public List<CrmActivityDto> findByContact(Long contactId) {
        return repo.findByContact_Id(contactId).stream().map(mapper::toDto).toList();
    }

    @Override @Transactional
    public CrmActivityDto create(CrmActivityDto dto) {
        CrmActivity e = mapper.toEntity(dto);
        resolveContact(e, dto);
        return mapper.toDto(repo.save(e));
    }

    @Override @Transactional
    public CrmActivityDto update(Long id, CrmActivityDto dto) {
        CrmActivity e = get(id);
        mapper.updateFromDto(dto, e);
        resolveContact(e, dto);
        return mapper.toDto(repo.save(e));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("CrmActivity", "id", id);
        repo.deleteById(id);
    }

    private CrmActivity get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CrmActivity", "id", id));
    }

    private void resolveContact(CrmActivity e, CrmActivityDto dto) {
        if (dto.getContactId() != null) {
            var con = contactRepo.findById(dto.getContactId())
                    .orElseThrow(() -> new ResourceNotFoundException("CrmContact", "id", dto.getContactId()));
            e.setContact(con);
            e.setContactName(con.getFirstName() + " " + con.getLastName());
            e.setAccountName(con.getCompany());
        }
    }
}

