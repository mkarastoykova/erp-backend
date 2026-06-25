package com.erp.payments.mapper;

import com.erp.common.dto.payments.PaymentTransactionDto;
import com.erp.common.entity.PaymentTransaction;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentTransactionMapper {
    PaymentTransactionDto toDto(PaymentTransaction e);
    PaymentTransaction toEntity(PaymentTransactionDto dto);
    void updateFromDto(PaymentTransactionDto dto, @MappingTarget PaymentTransaction e);
}

