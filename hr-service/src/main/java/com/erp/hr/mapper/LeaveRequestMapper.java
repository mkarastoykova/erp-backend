package com.erp.hr.mapper;

import com.erp.common.dto.LeaveRequestDto;
import com.erp.common.entity.LeaveRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LeaveRequestMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(lr.getEmployee().getFirstName() + ' ' + lr.getEmployee().getLastName())")
    @Mapping(target = "department", source = "employee.department")
    LeaveRequestDto toDto(LeaveRequest lr);

    @Mapping(target = "employee", ignore = true)
    LeaveRequest toEntity(LeaveRequestDto dto);

    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDto(LeaveRequestDto dto, @MappingTarget LeaveRequest entity);
}

