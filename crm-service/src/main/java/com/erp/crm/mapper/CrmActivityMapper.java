package com.erp.crm.mapper;

import com.erp.common.dto.crm.CrmActivityDto;
import com.erp.common.entity.CrmActivity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CrmActivityMapper {

    @Mapping(target = "contactId", source = "contact.id")
    CrmActivityDto toDto(CrmActivity e);

    @Mapping(target = "contact", ignore = true)
    CrmActivity toEntity(CrmActivityDto dto);

    @Mapping(target = "contact", ignore = true)
    void updateFromDto(CrmActivityDto dto, @MappingTarget CrmActivity e);
}

