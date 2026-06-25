package com.erp.hr.mapper;

import com.erp.common.dto.PayrollRecordDto;
import com.erp.common.entity.PayrollRecord;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PayrollRecordMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(pr.getEmployee().getFirstName() + ' ' + pr.getEmployee().getLastName())")
    @Mapping(target = "department", source = "employee.department")
    PayrollRecordDto toDto(PayrollRecord pr);

    @Mapping(target = "employee", ignore = true)
    PayrollRecord toEntity(PayrollRecordDto dto);

    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDto(PayrollRecordDto dto, @MappingTarget PayrollRecord entity);
}

