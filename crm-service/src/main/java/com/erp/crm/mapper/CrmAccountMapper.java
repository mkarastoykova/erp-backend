package com.erp.crm.mapper;

import com.erp.common.dto.crm.CrmAccountDto;
import com.erp.common.entity.CrmAccount;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CrmAccountMapper {
    CrmAccountDto toDto(CrmAccount e);
    CrmAccount toEntity(CrmAccountDto dto);
    void updateFromDto(CrmAccountDto dto, @MappingTarget CrmAccount e);
}

