package com.erp.finance.mapper;

import com.erp.common.dto.finance.TaxDefinitionDto;
import com.erp.common.entity.TaxDefinition;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaxDefinitionMapper {
    TaxDefinitionDto toDto(TaxDefinition entity);
    TaxDefinition toEntity(TaxDefinitionDto dto);
    void updateFromDto(TaxDefinitionDto dto, @MappingTarget TaxDefinition entity);
}

