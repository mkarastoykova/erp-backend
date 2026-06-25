package com.erp.crm.mapper;

import com.erp.common.dto.crm.SupportCaseDto;
import com.erp.common.entity.SupportCase;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SupportCaseMapper {

    @Mapping(target = "accountId", source = "account.id")
    SupportCaseDto toDto(SupportCase e);

    @Mapping(target = "account", ignore = true)
    SupportCase toEntity(SupportCaseDto dto);

    @Mapping(target = "account", ignore = true)
    void updateFromDto(SupportCaseDto dto, @MappingTarget SupportCase e);
}

