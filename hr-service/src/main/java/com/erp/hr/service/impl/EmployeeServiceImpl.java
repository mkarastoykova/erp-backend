package com.erp.hr.service.impl;

import com.erp.common.dto.EmployeeDto;
import com.erp.common.entity.Employee;
import com.erp.common.enums.EmployeeStatus;
import com.erp.hr.exception.DuplicateResourceException;
import com.erp.hr.exception.ResourceNotFoundException;
import com.erp.hr.mapper.EmployeeMapper;
import com.erp.hr.repository.EmployeeRepository;
import com.erp.hr.service.EmployeeService;
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
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeDto create(EmployeeDto dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", dto.getEmail());
        }
        if (employeeRepository.existsByEmployeeCode(dto.getEmployeeCode())) {
            throw new DuplicateResourceException("Employee", "employeeCode", dto.getEmployeeCode());
        }
        Employee employee = employeeMapper.toEntity(dto);
        if (dto.getManagerId() != null) {
            Employee manager = employeeRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", dto.getManagerId()));
            employee.setManager(manager);
        }
        Employee saved = employeeRepository.save(employee);
        log.info("Created employee [{}] with code {}", saved.getId(), saved.getEmployeeCode());
        return employeeMapper.toDto(saved);
    }

    @Override
    @Transactional
    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee employee = findEntityById(id);
        // Check email uniqueness only if it changed
        if (!employee.getEmail().equalsIgnoreCase(dto.getEmail())
                && employeeRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", dto.getEmail());
        }
        employeeMapper.updateEntityFromDto(dto, employee);
        if (dto.getManagerId() != null) {
            if (!dto.getManagerId().equals(id)) {
                Employee manager = employeeRepository.findById(dto.getManagerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", dto.getManagerId()));
                employee.setManager(manager);
            }
        } else {
            employee.setManager(null);
        }
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDto findById(Long id) {
        return employeeMapper.toDto(findEntityById(id));
    }

    @Override
    public EmployeeDto findByCode(String code) {
        return employeeMapper.toDto(
                employeeRepository.findByEmployeeCode(code)
                        .orElseThrow(() -> new ResourceNotFoundException("Employee", "code", code))
        );
    }

    @Override
    public Page<EmployeeDto> search(String department, EmployeeStatus status, String query, Pageable pageable) {
        return employeeRepository.searchEmployees(department, status, query, pageable)
                .map(employeeMapper::toDto);
    }

    @Override
    public List<EmployeeDto> findByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Override
    public List<EmployeeDto> findDirectReports(Long managerId) {
        findEntityById(managerId); // validate manager exists
        return employeeRepository.findByManagerId(managerId).stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee employee = findEntityById(id);
        log.info("Deleting employee [{}]", id);
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, EmployeeStatus status) {
        Employee employee = findEntityById(id);
        employee.setStatus(status);
        employeeRepository.save(employee);
        log.info("Updated employee [{}] status to {}", id, status);
    }

    private Employee findEntityById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }
}

