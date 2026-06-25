package com.erp.finance.service.impl;

import com.erp.common.dto.finance.JournalEntryDto;
import com.erp.finance.exception.ResourceNotFoundException;
import com.erp.finance.mapper.JournalEntryMapper;
import com.erp.finance.repository.JournalEntryRepository;
import com.erp.finance.service.JournalEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Slf4j
@Transactional(readOnly = true)
public class JournalEntryServiceImpl implements JournalEntryService {

    private final JournalEntryRepository repo;
    private final JournalEntryMapper mapper;

    @Override
    public Page<JournalEntryDto> search(String account, String q, Pageable pageable) {
        return repo.search(account, q, pageable).map(mapper::toDto);
    }

    @Override
    public JournalEntryDto findById(Long id) {
        return mapper.toDto(repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JournalEntry", "id", id)));
    }

    @Override @Transactional
    public JournalEntryDto create(JournalEntryDto dto) {
        return mapper.toDto(repo.save(mapper.toEntity(dto)));
    }

    @Override @Transactional
    public JournalEntryDto update(Long id, JournalEntryDto dto) {
        var entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JournalEntry", "id", id));
        mapper.updateFromDto(dto, entity);
        return mapper.toDto(repo.save(entity));
    }

    @Override @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new ResourceNotFoundException("JournalEntry", "id", id);
        repo.deleteById(id);
    }
}

