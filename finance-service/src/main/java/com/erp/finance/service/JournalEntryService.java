package com.erp.finance.service;

import com.erp.common.dto.finance.JournalEntryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalEntryService {
    Page<JournalEntryDto> search(String account, String q, Pageable pageable);
    JournalEntryDto findById(Long id);
    JournalEntryDto create(JournalEntryDto dto);
    JournalEntryDto update(Long id, JournalEntryDto dto);
    void delete(Long id);
}

