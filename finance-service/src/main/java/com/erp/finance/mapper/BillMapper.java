package com.erp.finance.mapper;

import com.erp.common.dto.finance.BillDto;
import com.erp.common.dto.finance.InvoiceLineDto;
import com.erp.common.entity.Bill;
import com.erp.common.entity.BillLine;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BillMapper {

    @Mapping(target = "lines", source = "lines")
    BillDto toDto(Bill bill);

    @Mapping(target = "lines", ignore = true)
    Bill toEntity(BillDto dto);

    @Mapping(target = "lines", ignore = true)
    void updateFromDto(BillDto dto, @MappingTarget Bill bill);

    InvoiceLineDto lineToDto(BillLine line);

    List<InvoiceLineDto> linesToDto(List<BillLine> lines);
}

