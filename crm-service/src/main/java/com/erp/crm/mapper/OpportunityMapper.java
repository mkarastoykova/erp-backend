package com.erp.crm.mapper;

import com.erp.common.dto.crm.OpportunityDto;
import com.erp.common.entity.Opportunity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OpportunityMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "contactId", source = "contact.id")
    OpportunityDto toDto(Opportunity e);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "contact", ignore = true)
    Opportunity toEntity(OpportunityDto dto);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "contact", ignore = true)
    void updateFromDto(OpportunityDto dto, @MappingTarget Opportunity e);
}

