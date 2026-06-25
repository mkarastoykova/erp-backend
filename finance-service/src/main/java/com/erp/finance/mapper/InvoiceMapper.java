package com.erp.finance.mapper;

import com.erp.common.dto.finance.InvoiceDto;
import com.erp.common.dto.finance.InvoiceLineDto;
import com.erp.common.entity.Invoice;
import com.erp.common.entity.InvoiceLine;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InvoiceMapper {

    @Mapping(target = "lines", source = "lines")
    InvoiceDto toDto(Invoice invoice);

    @Mapping(target = "lines", ignore = true)
    Invoice toEntity(InvoiceDto dto);

    @Mapping(target = "lines", ignore = true)
    void updateFromDto(InvoiceDto dto, @MappingTarget Invoice invoice);

    InvoiceLineDto lineToDto(InvoiceLine line);

    List<InvoiceLineDto> linesToDto(List<InvoiceLine> lines);
}

