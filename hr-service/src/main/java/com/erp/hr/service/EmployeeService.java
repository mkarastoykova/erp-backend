package com.erp.hr.service;

import com.erp.common.dto.EmployeeDto;
import com.erp.common.enums.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeDto create(EmployeeDto dto);

    EmployeeDto update(Long id, EmployeeDto dto);

    EmployeeDto findById(Long id);

    EmployeeDto findByCode(String code);

    Page<EmployeeDto> search(String department, EmployeeStatus status, String query, Pageable pageable);

    List<EmployeeDto> findByDepartment(String department);

    List<EmployeeDto> findDirectReports(Long managerId);

    void delete(Long id);

    void updateStatus(Long id, EmployeeStatus status);
}

