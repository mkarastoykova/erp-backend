package com.erp.hr.mapper;

import com.erp.common.dto.EmployeeBenefitDto;
import com.erp.common.entity.EmployeeBenefit;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeBenefitMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(eb.getEmployee().getFirstName() + ' ' + eb.getEmployee().getLastName())")
    @Mapping(target = "department", source = "employee.department")
    EmployeeBenefitDto toDto(EmployeeBenefit eb);

    @Mapping(target = "employee", ignore = true)
    EmployeeBenefit toEntity(EmployeeBenefitDto dto);

    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDto(EmployeeBenefitDto dto, @MappingTarget EmployeeBenefit entity);
}

