package com.erp.hr.service;

import com.erp.common.dto.EmployeeBenefitDto;
import com.erp.common.enums.BenefitStatus;
import com.erp.common.enums.BenefitType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeBenefitService {

    EmployeeBenefitDto create(EmployeeBenefitDto dto);

    EmployeeBenefitDto update(Long id, EmployeeBenefitDto dto);

    EmployeeBenefitDto findById(Long id);

    List<EmployeeBenefitDto> findByEmployee(Long employeeId);

    Page<EmployeeBenefitDto> search(Long employeeId, BenefitType type, BenefitStatus status, Pageable pageable);

    void delete(Long id);
}

