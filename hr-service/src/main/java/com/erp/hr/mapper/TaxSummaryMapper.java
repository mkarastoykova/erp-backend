package com.erp.hr.mapper;

import com.erp.common.dto.TaxSummaryDto;
import com.erp.common.entity.TaxSummary;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaxSummaryMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(ts.getEmployee().getFirstName() + ' ' + ts.getEmployee().getLastName())")
    @Mapping(target = "department", source = "employee.department")
    TaxSummaryDto toDto(TaxSummary ts);

    @Mapping(target = "employee", ignore = true)
    TaxSummary toEntity(TaxSummaryDto dto);

    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDto(TaxSummaryDto dto, @MappingTarget TaxSummary entity);
}

