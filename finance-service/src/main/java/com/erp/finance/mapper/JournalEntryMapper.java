package com.erp.finance.mapper;

import com.erp.common.dto.finance.JournalEntryDto;
import com.erp.common.entity.JournalEntry;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JournalEntryMapper {
    JournalEntryDto toDto(JournalEntry entity);
    JournalEntry toEntity(JournalEntryDto dto);
    void updateFromDto(JournalEntryDto dto, @MappingTarget JournalEntry entity);
}

