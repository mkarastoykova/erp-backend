package com.erp.hr.service.impl;

import com.erp.common.dto.SalaryHistoryDto;
import com.erp.common.entity.Employee;
import com.erp.common.entity.SalaryHistory;
import com.erp.hr.exception.ResourceNotFoundException;
import com.erp.hr.mapper.SalaryHistoryMapper;
import com.erp.hr.repository.EmployeeRepository;
import com.erp.hr.repository.SalaryHistoryRepository;
import com.erp.hr.service.SalaryHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SalaryHistoryServiceImpl implements SalaryHistoryService {

    private final SalaryHistoryRepository salaryHistoryRepository;
    private final EmployeeRepository employeeRepository;
    private final SalaryHistoryMapper salaryHistoryMapper;

    @Override
    @Transactional
    public SalaryHistoryDto create(SalaryHistoryDto dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", dto.getEmployeeId()));
        SalaryHistory entity = salaryHistoryMapper.toEntity(dto);
        entity.setEmployee(employee);
        // Update employee's current salary to new salary
        employee.setSalary(dto.getNewSalary());
        employeeRepository.save(employee);
        SalaryHistory saved = salaryHistoryRepository.save(entity);
        log.info("Created salary history [{}] for employee [{}]", saved.getId(), employee.getId());
        return salaryHistoryMapper.toDto(saved);
    }

    @Override
    public SalaryHistoryDto findById(Long id) {
        return salaryHistoryMapper.toDto(
                salaryHistoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("SalaryHistory", "id", id))
        );
    }

    @Override
    public List<SalaryHistoryDto> findByEmployee(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        return salaryHistoryRepository.findByEmployeeIdOrderByEffectiveDateDesc(employeeId).stream()
                .map(salaryHistoryMapper::toDto)
                .toList();
    }

    @Override
    public Page<SalaryHistoryDto> findByEmployeePaged(Long employeeId, Pageable pageable) {
        return salaryHistoryRepository.findByEmployeeId(employeeId, pageable)
                .map(salaryHistoryMapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SalaryHistory entity = salaryHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalaryHistory", "id", id));
        salaryHistoryRepository.delete(entity);
    }
}

