package com.erp.hr.mapper;

import com.erp.common.dto.EmployeeDto;
import com.erp.common.entity.Employee;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerName", expression = "java(employee.getManager() != null ? employee.getManager().getFirstName() + ' ' + employee.getManager().getLastName() : null)")
    EmployeeDto toDto(Employee employee);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "directReports", ignore = true)
    @Mapping(target = "leaveRequests", ignore = true)
    @Mapping(target = "payrollRecords", ignore = true)
    @Mapping(target = "salaryHistories", ignore = true)
    @Mapping(target = "benefits", ignore = true)
    @Mapping(target = "taxSummaries", ignore = true)
    @Mapping(target = "performanceReviews", ignore = true)
    Employee toEntity(EmployeeDto dto);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "directReports", ignore = true)
    @Mapping(target = "leaveRequests", ignore = true)
    @Mapping(target = "payrollRecords", ignore = true)
    @Mapping(target = "salaryHistories", ignore = true)
    @Mapping(target = "benefits", ignore = true)
    @Mapping(target = "taxSummaries", ignore = true)
    @Mapping(target = "performanceReviews", ignore = true)
    void updateEntityFromDto(EmployeeDto dto, @MappingTarget Employee employee);
}

