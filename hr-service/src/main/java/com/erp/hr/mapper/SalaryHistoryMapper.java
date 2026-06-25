package com.erp.hr.mapper;

import com.erp.common.dto.SalaryHistoryDto;
import com.erp.common.entity.SalaryHistory;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SalaryHistoryMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(sh.getEmployee().getFirstName() + ' ' + sh.getEmployee().getLastName())")
    @Mapping(target = "department", source = "employee.department")
    SalaryHistoryDto toDto(SalaryHistory sh);

    @Mapping(target = "employee", ignore = true)
    SalaryHistory toEntity(SalaryHistoryDto dto);

    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDto(SalaryHistoryDto dto, @MappingTarget SalaryHistory entity);
}

