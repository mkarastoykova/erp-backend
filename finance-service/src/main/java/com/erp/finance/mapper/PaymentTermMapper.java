package com.erp.finance.mapper;

import com.erp.common.dto.finance.PaymentTermDefDto;
import com.erp.common.entity.PaymentTermDef;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentTermMapper {
    PaymentTermDefDto toDto(PaymentTermDef entity);
    PaymentTermDef toEntity(PaymentTermDefDto dto);
    void updateFromDto(PaymentTermDefDto dto, @MappingTarget PaymentTermDef entity);
}

