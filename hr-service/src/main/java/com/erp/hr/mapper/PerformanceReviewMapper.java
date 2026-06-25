package com.erp.hr.mapper;

import com.erp.common.dto.PerformanceReviewDto;
import com.erp.common.entity.PerformanceReview;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PerformanceReviewMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(pr.getEmployee().getFirstName() + ' ' + pr.getEmployee().getLastName())")
    @Mapping(target = "department", source = "employee.department")
    PerformanceReviewDto toDto(PerformanceReview pr);

    @Mapping(target = "employee", ignore = true)
    PerformanceReview toEntity(PerformanceReviewDto dto);

    @Mapping(target = "employee", ignore = true)
    void updateEntityFromDto(PerformanceReviewDto dto, @MappingTarget PerformanceReview entity);
}

