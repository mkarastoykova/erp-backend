package com.erp.hr.service.impl;

import com.erp.common.dto.EmployeeBenefitDto;
import com.erp.common.entity.Employee;
import com.erp.common.entity.EmployeeBenefit;
import com.erp.common.enums.BenefitStatus;
import com.erp.common.enums.BenefitType;
import com.erp.hr.exception.ResourceNotFoundException;
import com.erp.hr.mapper.EmployeeBenefitMapper;
import com.erp.hr.repository.EmployeeBenefitRepository;
import com.erp.hr.repository.EmployeeRepository;
import com.erp.hr.service.EmployeeBenefitService;
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
public class EmployeeBenefitServiceImpl implements EmployeeBenefitService {

    private final EmployeeBenefitRepository benefitRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeBenefitMapper benefitMapper;

    @Override
    @Transactional
    public EmployeeBenefitDto create(EmployeeBenefitDto dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", dto.getEmployeeId()));
        EmployeeBenefit entity = benefitMapper.toEntity(dto);
        entity.setEmployee(employee);
        EmployeeBenefit saved = benefitRepository.save(entity);
        log.info("Created benefit [{}] for employee [{}]", saved.getId(), employee.getId());
        return benefitMapper.toDto(saved);
    }

    @Override
    @Transactional
    public EmployeeBenefitDto update(Long id, EmployeeBenefitDto dto) {
        EmployeeBenefit entity = findEntityById(id);
        benefitMapper.updateEntityFromDto(dto, entity);
        return benefitMapper.toDto(benefitRepository.save(entity));
    }

    @Override
    public EmployeeBenefitDto findById(Long id) {
        return benefitMapper.toDto(findEntityById(id));
    }

    @Override
    public List<EmployeeBenefitDto> findByEmployee(Long employeeId) {
        return benefitRepository.findByEmployeeId(employeeId).stream()
                .map(benefitMapper::toDto)
                .toList();
    }

    @Override
    public Page<EmployeeBenefitDto> search(Long employeeId, BenefitType type, BenefitStatus status, Pageable pageable) {
        return benefitRepository.searchBenefits(employeeId, type, status, pageable)
                .map(benefitMapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EmployeeBenefit entity = findEntityById(id);
        benefitRepository.delete(entity);
    }

    private EmployeeBenefit findEntityById(Long id) {
        return benefitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeBenefit", "id", id));
    }
}

