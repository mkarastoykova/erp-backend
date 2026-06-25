package com.erp.crm.mapper;

import com.erp.common.dto.crm.CrmContactDto;
import com.erp.common.entity.CrmContact;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CrmContactMapper {

    @Mapping(target = "accountId", source = "account.id")
    CrmContactDto toDto(CrmContact e);

    @Mapping(target = "account", ignore = true)
    CrmContact toEntity(CrmContactDto dto);

    @Mapping(target = "account", ignore = true)
    void updateFromDto(CrmContactDto dto, @MappingTarget CrmContact e);
}

