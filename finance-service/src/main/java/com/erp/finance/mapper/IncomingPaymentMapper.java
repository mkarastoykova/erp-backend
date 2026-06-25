package com.erp.finance.mapper;

import com.erp.common.dto.finance.IncomingPaymentDto;
import com.erp.common.entity.IncomingPayment;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface IncomingPaymentMapper {
    IncomingPaymentDto toDto(IncomingPayment entity);
    IncomingPayment toEntity(IncomingPaymentDto dto);
    void updateFromDto(IncomingPaymentDto dto, @MappingTarget IncomingPayment entity);
}

