package com.erp.finance.mapper;

import com.erp.common.dto.finance.OutgoingPaymentDto;
import com.erp.common.entity.OutgoingPayment;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OutgoingPaymentMapper {
    OutgoingPaymentDto toDto(OutgoingPayment entity);
    OutgoingPayment toEntity(OutgoingPaymentDto dto);
    void updateFromDto(OutgoingPaymentDto dto, @MappingTarget OutgoingPayment entity);
}

